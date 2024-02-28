package de.intagau.issue.controller;

import de.intagau.issue.entity.Issue;
import de.intagau.issue.service.IssueService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/issue")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping
    public ResponseEntity<List<Issue>> listIssues() {
        return ResponseEntity.ok(issueService.listIssues());
    }

    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue){
        Issue savedIssue = issueService.createIssue(issue);
        return new ResponseEntity<>(savedIssue, HttpStatus.CREATED);
    }

    @PostMapping("undo")
    public void undo() {
        issueService.undo();
    }

    @DeleteMapping("{id}")
    public void deleteIssue(@PathVariable("id") Long id) {
        issueService.removeIssue(id);
    }
}
