import { Dropdown, DropdownOption, BasicDropdownOption } from "./dropdown";
import {
  makeCell,
  addColumnHeader,
  addIconButton,
  CellStatus,
  makeIcon,
  shadeElement,
} from "../util/html-utils";
import { toggleLegend } from "./legend";
import { post } from "../util/requests";
import { Pair } from "../util/pair";
import { TextInput } from "./text-input";
import { showErrorDialog } from "./dialog";

type SortType = "number" | "text" | "date";
type FilterType = "text" | "dropdown";

export interface ColumnDefinition<ParentType, ChildType> {
  title: string;
  sortType?: SortType;
  child?: boolean;
  addParentContents?: (object: ParentType, fragment: DocumentFragment) => void;
  addChildContents?: (
    object: ChildType,
    parent: ParentType,
    fragment: DocumentFragment
  ) => void;
  getCellHighlight?: (
    object: ParentType,
    child: ChildType | null
  ) => CellStatus | null;
}

export interface SortDefinition {
  columnTitle: string;
  descending: boolean;
  type: SortType;
}

export interface FilterDefinition {
  title: string; // user friendly label
  key: string; // internal use
  type: FilterType; // either text or dropdown
  values?: string[]; // required for dropdown filters
  autocompleteUrl?: string; // required for text filters w/autocomplete
}

export interface StaticAction {
  title: string;
  handler: () => void;
}

export interface BulkAction<ParentType> {
  title: string;
  handler: (items: ParentType[]) => void;
}

export interface TableDefinition<ParentType, ChildType> {
  queryUrl: string;
  defaultSort?: SortDefinition;
  filters?: FilterDefinition[];
  getChildren?: (parent: ParentType) => ChildType[];
  generateColumns: (
    data?: ParentType[]
  ) => ColumnDefinition<ParentType, ChildType>[];
  getRowHighlight?: (object: ParentType) => CellStatus | null;
  staticActions?: StaticAction[];
  bulkActions?: BulkAction<ParentType>[];
  disablePageControls?: boolean;
}

class AcceptedFilter {
  element: HTMLElement;
  key: string;
  value: string;
  valid: boolean = true;

  constructor(title: string, key: string, value: string, onRemove: () => void) {
    this.key = key;
    this.value = value;
    this.element = document.createElement("span");
    this.element.className =
      "font-inter font-medium text-12 text-black bg-grey-100 px-2 py-1 rounded-md cursor-default inline-block";
    this.element.innerHTML = `${title}: ${value}`;

    const destroyFilterIcon = makeIcon("xmark");
    destroyFilterIcon.classList.add(
      "text-black",
      "cursor-pointer",
      "ml-2",
      "hover:text-green-200"
    );
    destroyFilterIcon.onclick = () => {
      this.valid = false;
      this.element.remove();
      onRemove();
    };
    this.element.appendChild(destroyFilterIcon);
  }
}

export class TableBuilder<ParentType, ChildType> {
  definition: TableDefinition<ParentType, ChildType>;
  container: HTMLElement;
  table?: HTMLTableElement;
  thead?: HTMLTableSectionElement;
  pageDescription?: HTMLSpanElement;
  pageLeftButton?: HTMLButtonElement;
  pageRightButton?: HTMLButtonElement;

  columns: ColumnDefinition<ParentType, ChildType>[];
  sortColumn?: string;
  sortDescending: boolean;
  pageSize: number = 10;
  pageNumber: number = 1;
  totalCount: number = 0;
  filteredCount: number = 0;
  baseFilterKey: string | null;
  baseFilterValue: string | null;
  acceptedFilters: AcceptedFilter[] = [];
  allItems: ParentType[] = [];
  selectedItems: Set<ParentType> = new Set<ParentType>();
  selectAllCheckbox?: HTMLInputElement;
  onFilterChange?: (key: string, value: string, add?: boolean) => void;

  constructor(
    definition: TableDefinition<ParentType, ChildType>,
    containerId: string,
    filterParams?: Array<Pair<string, string>>,
    onFilterChange?: (key: string, value: string, add?: boolean) => void
  ) {
    this.definition = definition;
    if (definition.defaultSort) {
      this.sortColumn = definition.defaultSort.columnTitle;
    }
    this.sortDescending = definition.defaultSort
      ? definition.defaultSort.descending
      : false;
    const container = document.getElementById(containerId);
    if (container === null) {
      throw Error(`Container ID "${containerId}" not found on page`);
    }
    this.baseFilterKey = container.getAttribute("data-detail-type");
    this.baseFilterValue = container.getAttribute("data-detail-value");

    if (filterParams) {
      // create accepted filter if there is a matching key in table definition
      filterParams.forEach((p) => {
        this.definition.filters?.forEach((f) => {
          if (f.key === p.key) {
            // filter key is valid, create a new accepted filter
            const onRemove = () => {
              if (this.onFilterChange)
                this.onFilterChange(p.key, p.value, false);
              this.reload();
            };
            this.acceptedFilters.push(
              new AcceptedFilter(f.title, f.key, p.value, onRemove)
            );
          }
        });
      });
    }
    this.onFilterChange = onFilterChange;
    this.container = container;
    this.columns = definition.generateColumns();
  }

  public build() {
    const topControlsContainer = document.createElement("div");
    topControlsContainer.className = "flex mt-4 items-top space-x-2";
    if (!this.definition.disablePageControls) {
      this.addSortControls(topControlsContainer);
      this.addFilterControls(topControlsContainer);
      this.addPagingControls(topControlsContainer);
    }
    this.container.appendChild(topControlsContainer);
    const tableContainer = document.createElement("div");
    tableContainer.className = "mt-4 overflow-auto";
    this.table = document.createElement("table");
    // set global default styling settings
    this.table.classList.add(
      "w-full",
      "text-14",
      "text-black",
      "font-medium",
      "font-inter",
      "border-separate",
      "border-spacing-0",
      "border-grey-200",
      "border-2",
      "rounded-xl",
      "overflow-hidden"
    );

    tableContainer.appendChild(this.table);
    this.container.appendChild(tableContainer);

    const bottomControlsContainer = document.createElement("div");
    bottomControlsContainer.className =
      "flex justify-end mt-4 items-top space-x-2";
    this.addActionButtons(bottomControlsContainer);
    this.container.appendChild(bottomControlsContainer);
    this.load();
    this.setupScrollListener();
    this.reload();
  }

  private addActionButtons(container: HTMLElement) {
    if (this.definition.bulkActions) {
      this.definition.bulkActions.forEach((action) => {
        this.addActionButton(container, action.title, () => {
          if (!this.selectedItems.size) {
            showErrorDialog("No items selected");
            return;
          }
          action.handler(Array.from(this.selectedItems));
        });
      });
    }
    if (this.definition.staticActions) {
      this.definition.staticActions.forEach((action) => {
        this.addActionButton(container, action.title, action.handler);
      });
    }
  }

  private addActionButton(
    container: HTMLElement,
    title: string,
    handler: () => void
  ) {
    const button = document.createElement("button");
    button.className =
      "bg-green-200 rounded-md hover:ring-2 ring-offset-1 ring-green-200 text-white font-inter font-medium text-12 px-2 py-1";
    button.innerText = title;
    button.onclick = handler;
    container.appendChild(button);
  }

  private addSortControls(container: HTMLElement) {
    const sortContainer = document.createElement("div");
    sortContainer.classList.add("flex-none", "space-x-2");
    container.appendChild(sortContainer);
    // add sort icon
    const icon = makeIcon("sort");
    icon.title = "Sort";
    icon.classList.add("text-black");
    sortContainer.appendChild(icon);

    // adds all dropdown items
    let dropdownOptions: DropdownOption[] = [];
    this.columns
      .filter((column) => column.sortType)
      .forEach((column) => {
        dropdownOptions.push(this.addSortOption(column, false));
        dropdownOptions.push(this.addSortOption(column, true));
      });

    const defaultOption = this.definition.defaultSort
      ? this.definition.defaultSort.columnTitle +
        " - " +
        this.getSortDescriptor(
          this.definition.defaultSort.type,
          this.definition.defaultSort.descending
        )
      : "undefined";
    const sortDropdown = new Dropdown(
      dropdownOptions,
      true,
      undefined,
      defaultOption
    );
    sortContainer.appendChild(sortDropdown.getContainerTag());
  }

  private addSortOption(
    column: ColumnDefinition<ParentType, ChildType>,
    descending: boolean
  ) {
    const label =
      column.title +
      " - " +
      this.getSortDescriptor(column.sortType, descending);

    return new BasicDropdownOption(label, () => {
      this.sortColumn = column.title;
      this.sortDescending = descending;
      this.reload();
    });
  }

  private getSortDescriptor(
    sortType: SortType | undefined,
    descending: boolean
  ) {
    switch (sortType) {
      case "date":
        return descending ? "Latest First" : "Latest Last";
      case "number":
        return descending ? "High to Low" : "Low to High";
      case "text":
        return descending ? "Z to A" : "A to Z";
      default:
        throw new Error(`Unhandled sort type: ${sortType}`);
    }
  }

  private addFilterControls(container: HTMLElement) {
    const filterContainer = document.createElement("div");
    filterContainer.classList.add("flex-auto", "items-center", "space-x-2");
    container.appendChild(filterContainer);
    if (!this.definition.filters) {
      // no filters for this table
      return;
    }
    // add filter icon
    const filterIcon = makeIcon("filter");
    filterIcon.title = "Filter";
    filterIcon.classList.add("text-black");
    filterContainer.appendChild(filterIcon);

    // add pre-table build filters
    for (var filter of this.acceptedFilters) {
      filterContainer.appendChild(filter.element);
    }

    let filterOptions: DropdownOption[] = [];
    this.definition.filters.forEach((filter) => {
      switch (filter.type) {
        case "dropdown":
          filterOptions.push(this.makeDropdownFilter(filter, filterContainer));
          break;
        case "text":
          filterOptions.push(this.makeTextInputFilter(filter, filterContainer));
          break;
        default:
          throw new Error(`Unhandled filter type: ${filter.type}`);
      }
    });

    const addFilterDropdown = new Dropdown(
      filterOptions,
      false,
      undefined,
      "+ filter"
    );
    filterContainer.appendChild(addFilterDropdown.getContainerTag());
  }

  private makeDropdownFilter(
    filter: FilterDefinition,
    filterContainer: HTMLElement
  ) {
    if (!filter.values || !filter.values.length) {
      throw new Error(`Dropdown filter ${filter.key} has no dropdown options`);
    }
    // create all submenu options
    const filterSuboptions = filter.values.map(
      (value) =>
        new BasicDropdownOption(value, (dropdown: Dropdown) => {
          dropdown.getContainerTag().remove();
          const onRemove = () => {
            if (this.onFilterChange)
              this.onFilterChange(filter.key, value, false);
            this.reload(true);
          };
          const filterLabel = new AcceptedFilter(
            filter.title,
            filter.key,
            value,
            onRemove
          );
          // add filter label to the menu bar
          filterContainer.insertBefore(
            filterLabel.element,
            filterContainer.lastChild
          );
          this.acceptedFilters.push(filterLabel);
          // update params
          if (this.onFilterChange) {
            this.onFilterChange(filter.key, value, true);
          }
          this.reload(true); // apply new filter
        })
    );
    // add filter (& its submenu) to the parent filter menu
    return new BasicDropdownOption(filter.title, () => {
      const filterSuboptionsDropdown = new Dropdown(
        filterSuboptions,
        true,
        filter.title,
        undefined,
        true
      );
      filterContainer.insertBefore(
        filterSuboptionsDropdown.getContainerTag(),
        filterContainer.lastChild
      );
    });
  }

  private makeTextInputFilter(
    filter: FilterDefinition,
    filterContainer: HTMLElement
  ) {
    const onClose = (textInput: TextInput) => {
      const onRemove = () => {
        if (this.onFilterChange)
          this.onFilterChange(filter.key, textInput.getValue(), false);
        this.reload(true);
      };
      const filterLabel = new AcceptedFilter(
        filter.title,
        filter.key,
        textInput.getValue(),
        onRemove
      );
      textInput.getContainerTag().remove();
      filterContainer.insertBefore(
        filterLabel.element,
        filterContainer.lastChild
      );
      this.acceptedFilters.push(filterLabel);
      // update params
      if (this.onFilterChange) {
        this.onFilterChange(filter.key, textInput.getValue(), true);
      }
      this.reload(true); // apply new filter
    };
    return new BasicDropdownOption(filter.title, () => {
      if (!filter.autocompleteUrl) {
        throw new Error(
          `Text input filter ${filter.title} has no autocomplete rest URL`
        );
      }
      const filterTextInput = new TextInput(
        filter.title,
        onClose,
        filter.autocompleteUrl
      );
      filterContainer.insertBefore(
        filterTextInput.getContainerTag(),
        filterContainer.lastChild
      );
    });
  }

  private addPagingControls(container: HTMLElement) {
    const pagingContainer = document.createElement("div");
    pagingContainer.classList.add("flex-none", "space-x-2");
    container.appendChild(pagingContainer);

    const pagingIcon = makeIcon("book-open");
    pagingIcon.title = "Page";
    pagingIcon.classList.add("text-black");
    pagingContainer.appendChild(pagingIcon);

    const pageSizeOptions = [10, 30, 50, 75, 100, 250, 500, 1000].map(
      (pageSize) =>
        new BasicDropdownOption(pageSize.toString(), () => {
          const topItemNumber = (this.pageNumber - 1) * this.pageSize + 1;
          this.pageSize = pageSize;
          // keep current top item in view
          this.pageNumber = Math.floor(topItemNumber / pageSize) + 1;
          this.reload();
        })
    );
    const pageSizeSelectDropdown = new Dropdown(
      pageSizeOptions,
      true,
      "Items per page",
      this.pageSize.toString()
    );
    pagingContainer.appendChild(pageSizeSelectDropdown.getContainerTag());

    this.pageDescription = document.createElement("span");
    this.pageDescription.className =
      "font-inter font-medium text-black text-12";
    pagingContainer.appendChild(this.pageDescription);

    this.pageLeftButton = addIconButton(pagingContainer, "angle-left");
    this.pageLeftButton.disabled = true;
    this.pageLeftButton.onclick = (event) => {
      this.pageNumber--;
      this.reload();
    };
    this.pageRightButton = addIconButton(pagingContainer, "angle-right");
    this.pageRightButton.disabled = true;
    this.pageRightButton.onclick = (event) => {
      this.pageNumber++;
      this.reload();
    };
  }

  private load(data?: ParentType[]) {
    this.columns = this.definition.generateColumns(data);
    const table = getElement(this.table);
    table.replaceChildren();
    this.addTableHead(table);
    this.addTableBody(table, data);
    this.allItems = data || [];
    this.selectedItems = new Set<ParentType>();
  }

  private addTableHead(table: HTMLTableElement) {
    this.thead = table.createTHead();
    this.thead.className = "relative";
    const row = this.thead.insertRow();
    if (this.definition.bulkActions) {
      this.addSelectAllHeader(row);
    } else {
      this.selectAllCheckbox = undefined;
    }
    this.columns.forEach((column, i) => {
      addColumnHeader(
        row,
        column.title,
        i == 0 && !this.definition.bulkActions
      );
    });
  }

  private addSelectAllHeader(thead: HTMLTableRowElement) {
    const th = document.createElement("th");
    th.className =
      "p-4 text-white font-semibold bg-grey-300 text-left align-text-top";
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.onchange = (event) => {
      this.toggleSelectAll(checkbox.checked);
    };
    this.selectAllCheckbox = checkbox;
    th.appendChild(checkbox);
    thead.appendChild(th);
  }

  private toggleSelectAll(select: boolean) {
    if (select) {
      this.allItems.forEach((item) => this.selectedItems.add(item));
    } else {
      this.selectedItems = new Set<ParentType>();
    }
    const rowSelects = this.container.getElementsByClassName("row-select");
    Array.prototype.forEach.call(
      rowSelects,
      (rowSelect) => (rowSelect.checked = select)
    );
  }

  private addTableBody(table: HTMLTableElement, data?: ParentType[]) {
    const tbody = table.createTBody();
    if (data && data.length) {
      data.forEach((parent) => {
        this.addDataRow(tbody, parent);
      });
    } else {
      this.addNoDataRow(tbody);
    }
  }

  private setupScrollListener() {
    document.addEventListener("scroll", (event) => {
      const table = getElement(this.table);
      const thead = getElement(this.thead);
      const tableRect = table.getBoundingClientRect();
      if (tableRect.y >= 0) {
        thead.style.top = "0";
        return;
      }
      const headRect = thead.getBoundingClientRect();
      if (headRect.height - 2 > tableRect.bottom) {
        thead.style.top = tableRect.height - headRect.height + "px";
      } else {
        thead.style.top = tableRect.top * -1 - 2 + "px";
      }
    });
  }

  private async reload(resetPage?: boolean) {
    this.showLoading();
    if (resetPage) {
      this.pageNumber = 1;
    }
    this.acceptedFilters = this.acceptedFilters.filter(
      (filter) => filter.valid
    );
    const response = await post(this.definition.queryUrl, {
      pageSize: this.pageSize,
      pageNumber: this.pageNumber,
      baseFilter: this.baseFilterKey
        ? { key: this.baseFilterKey, value: this.baseFilterValue }
        : null,
      filters: this.acceptedFilters.map((filter) => {
        return { key: filter.key, value: filter.value };
      }),
      sortColumn: this.sortColumn,
      descending: this.sortDescending,
    });
    if (!response.ok) {
      throw new Error(`Error reloading table: ${response.status}`);
    }
    const data = await response.json();
    this.load(data.items);
    this.showLoaded(data);
  }

  private showLoading() {
    // TODO: Disable all inputs, show indeterminate progress
    if (!this.definition.disablePageControls) {
      getElement(this.pageLeftButton).disabled = true;
      getElement(this.pageRightButton).disabled = true;
    }
  }

  private showLoaded(data: any) {
    if (!this.definition.disablePageControls) {
      if (this.pageNumber > 1) {
        getElement(this.pageLeftButton).disabled = false;
      }
      if (data.filteredCount > this.pageSize * this.pageNumber) {
        getElement(this.pageRightButton).disabled = false;
      }
      const pageStart = this.pageSize * (this.pageNumber - 1) + 1;
      const pageEnd = Math.min(
        this.pageSize * this.pageNumber,
        data.filteredCount
      );
      let pageDescriptionText = `${pageStart}-${pageEnd} of ${data.filteredCount}`;
      if (data.filteredCount < data.totalCount) {
        pageDescriptionText += ` (filtered from ${data.totalCount})`;
      }
      getElement(this.pageDescription).textContent = pageDescriptionText;
    }
  }

  private addDataRow(tbody: HTMLTableSectionElement, parent: ParentType) {
    let children: ChildType[] = [];
    if (this.definition.getChildren) {
      children = this.definition.getChildren(parent);
    }
    // generate parent row, which includes the first child (if applicable)
    const tr = this.addBodyRow(tbody, parent);
    if (this.definition.bulkActions) {
      this.addRowSelectCell(tr, parent);
    }
    this.columns.forEach((column, i) => {
      if (column.child) {
        if (children.length) {
          this.addChildCell(tr, column, children[0], parent, i);
        } else {
          const td = makeCell(tr, i == 0 && !this.definition.bulkActions);
          shadeElement(td, "na");
          td.appendChild(document.createTextNode("N/A"));
        }
      } else {
        this.addParentCell(tr, column, parent, children, i);
      }
    });
    if (children.length > 1) {
      // generate additional child rows
      children.forEach((child, i) => {
        if (i === 0) {
          // first child already added with parent
          return;
        }
        const tr = this.addBodyRow(tbody, parent);
        this.columns.forEach((column) => {
          if (column.child) {
            this.addChildCell(tr, column, child, parent, i);
          }
        });
      });
    }
  }

  private addBodyRow(tbody: HTMLTableSectionElement, parent: ParentType) {
    const tr = tbody.insertRow();
    if (this.definition.getRowHighlight) {
      shadeElement(tr, this.definition.getRowHighlight(parent));
    }
    return tr;
  }

  private addNoDataRow(tbody: HTMLTableSectionElement) {
    const row = tbody.insertRow();
    const cell = makeCell(row, true);
    cell.colSpan = this.columns.length + (this.definition.bulkActions ? 1 : 0);
    cell.classList.add("bg-grey-100", "font-bold");
    cell.appendChild(document.createTextNode("NO DATA"));
  }

  private addRowSelectCell(tr: HTMLTableRowElement, item: ParentType) {
    const td = makeCell(tr, true);
    const checkbox = document.createElement("input");
    checkbox.className = "row-select";
    checkbox.type = "checkbox";
    checkbox.onchange = (event) => {
      if (checkbox.checked) {
        this.selectedItems.add(item);
      } else {
        if (this.selectAllCheckbox) {
          this.selectAllCheckbox.checked = false;
          this.selectedItems.delete(item);
        }
      }
    };
    td.appendChild(checkbox);
  }

  private addParentCell(
    tr: HTMLTableRowElement,
    column: ColumnDefinition<ParentType, ChildType>,
    parent: ParentType,
    children: ChildType[],
    index: number
  ) {
    if (!column.addParentContents) {
      throw new Error(
        `Column "${column.title}" specified as parent, but doesn't define addParentContents`
      );
    }
    const td = makeCell(tr, index == 0 && !this.definition.bulkActions);
    if (column.getCellHighlight) {
      shadeElement(td, column.getCellHighlight(parent, null));
    }
    const fragment = document.createDocumentFragment();
    column.addParentContents(parent, fragment);
    td.appendChild(fragment);
    if (children.length > 1) {
      td.rowSpan = children.length;
    }
  }

  private addChildCell(
    tr: HTMLTableRowElement,
    column: ColumnDefinition<ParentType, ChildType>,
    child: ChildType,
    parent: ParentType,
    index: number
  ) {
    if (!column.addChildContents) {
      throw new Error(
        `Column "${column.title}" specified as child, but doesn't define getChildContents`
      );
    }
    const td = makeCell(tr, index == 0 && !this.definition.bulkActions);
    if (column.getCellHighlight) {
      shadeElement(td, column.getCellHighlight(parent, child));
    }
    const fragment = document.createDocumentFragment();
    column.addChildContents(child, parent, fragment);
    td.appendChild(fragment);
  }
}

function getElement<Type>(element?: Type) {
  if (element) {
    return element;
  } else {
    throw new Error("Missing element");
  }
}

export const legendAction: StaticAction = {
  title: "Legend",
  handler: toggleLegend,
};
