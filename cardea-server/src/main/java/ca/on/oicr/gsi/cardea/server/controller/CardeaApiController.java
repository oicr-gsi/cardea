package ca.on.oicr.gsi.cardea.server.controller;

import ca.on.oicr.gsi.cardea.server.service.CaseService;
import ca.on.oicr.gsi.cardea.data.CaseData;
import ca.on.oicr.gsi.cardea.data.CaseStatusCountsForRun;
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

  @GetMapping("/case-status-counts/{runName}")
  public CaseStatusCountsForRun getCaseStatusCountsForRun(@PathVariable String runName) {
    return caseService.getCaseStatusCountsForRun(runName);
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

  @GetMapping("/shesmu-cases")
  public Set<ShesmuCase> getShesmuCases() {
    return caseService.getShesmuCases();
  }

}
