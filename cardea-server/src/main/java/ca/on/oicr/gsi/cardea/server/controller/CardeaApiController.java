package ca.on.oicr.gsi.cardea.server.controller;

import ca.on.oicr.gsi.cardea.server.service.CaseService;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.CaseStatusesForRun;
import ca.on.oicr.gsi.cardea.data.CasesForRequisition;
import ca.on.oicr.gsi.cardea.data.ShesmuCase;

import java.time.ZonedDateTime;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CardeaApiController {
  @Autowired
  private CaseService caseService;

  @GetMapping("/case-statuses/{runName}")
  public CaseStatusesForRun getCaseStatusesForRun(@PathVariable String runName) {
    return caseService.getCaseStatusesForRun(runName);
  }

  @GetMapping("/dimsum")
  public CaseData getDimsumData() {
    return caseService.getCaseData();
  }

  @GetMapping("/timestamp")
  public ZonedDateTime getDataTimestamp() {
    CaseData caseData = caseService.getCaseData();
    if (caseData == null) {
      return null;
    } else {
      return caseData.getTimestamp();
    }
  }

  @GetMapping("/requisition-cases/{requisitionName}")
  public CasesForRequisition getCasesForRequisition(@PathVariable String requisitionName) {
    if (requisitionName == null || requisitionName.isBlank()) {
      throw new BadRequestException("missing requisition name in URL");
    }
    CasesForRequisition casesForRequisition = caseService.getCasesForRequisition(requisitionName);
    if (casesForRequisition == null) {
      throw new NotFoundException(String.format("could not find requisition with name '%s'", requisitionName));
    }
    return casesForRequisition;
  }

  @GetMapping("/shesmu-cases")
  public Set<ShesmuCase> getShesmuCases() {
    return caseService.getShesmuCases();
  }

}
