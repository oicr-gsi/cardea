package ca.on.oicr.gsi.cardea.server.controller;

import ca.on.oicr.gsi.cardea.server.service.CaseService;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.CaseStatusesForRun;
import ca.on.oicr.gsi.cardea.data.DjerbaCases;
import ca.on.oicr.gsi.cardea.data.ShesmuCase;

import java.time.ZonedDateTime;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

  @GetMapping("/djerba-cases/{requisitionName}")
  public DjerbaCases getDjerbaCases(@PathVariable String requisitionName) {
    if (requisitionName == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "must provide requisition name in URL");
    }
    DjerbaCases djerbaCases = caseService.getDjerbaCases(requisitionName);
    if (djerbaCases == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "could not find requisition with name " + requisitionName);
    }
    return djerbaCases;
  }

  @GetMapping("/shesmu-cases")
  public Set<ShesmuCase> getShesmuCases() {
    return caseService.getShesmuCases();
  }

}
