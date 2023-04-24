package ca.on.oicr.gsi.cardea.server.controller;

import ca.on.oicr.gsi.cardea.server.service.CaseService;
import ca.on.oicr.gsi.qcgateetlapi.data.CaseData;
import ca.on.oicr.gsi.qcgateetlapi.data.CaseStatusCountsForRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class QcGateEtlApiController {
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

}
