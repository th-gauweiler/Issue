package de.intagau.issue.service;

import de.intagau.issue.entity.Command;
import de.intagau.issue.entity.Issue;
import de.intagau.issue.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    private List<Issue> issueList = new ArrayList<>();
    private List<Command> commandList = new ArrayList<>();

    public Issue createIssue(Issue issue) {
        createIssueWoCommandlist(issue);
        commandList.add(new Command("create", issue,
                () -> this.removeIssueWithoutCommandList(commandList.removeLast().getIssue().getId())));
        return issue;
    }

    private void createIssueWoCommandlist(Issue issue) {
        issue.setId((long)UUID.randomUUID().hashCode());
        issueList.add(issue);
    }

    public void removeIssue(Long issueId) {
        Issue issue = issueList.stream()
                .filter(anIssue -> issueId.equals(anIssue.getId()))
                .findFirst().get();
        commandList.add(new Command("remove", issue,
                () -> this.createIssueWoCommandlist(commandList.removeLast().getIssue())));
        removeIssueWithoutCommandList(issueId);
    }

    private void removeIssueWithoutCommandList(Long issueId) {
        issueList = issueList.stream()
                .filter(anIssue -> !issueId.equals(anIssue.getId()))
                .collect(Collectors.toList());
    }

    public void undo() {
        Command lastCommand = commandList.getLast();
        lastCommand.getCode().run();
//        if (lastCommand.getCommand().equals("create")) {
//            this.removeIssueWithoutCommandList(lastCommand.getIssue().getId());
//        }
//        if (lastCommand.getCommand().equals("remove")) {
//            this.createIssueWoCommandlist(lastCommand.getIssue());
//        }
        // if statements als eigenen Code im Commandobjekt hinterlegen
        // für redo weitere Liste aufbauen
    }

    public List<Issue> listIssues() {
        return issueList;
    }
}
