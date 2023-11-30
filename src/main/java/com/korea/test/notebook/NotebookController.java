package com.korea.test.notebook;

import com.korea.test.notePage.NotePage;
import com.korea.test.notePage.NotePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notebook")
@RequiredArgsConstructor
public class NotebookController {

    private final NotePageService notePageService;
    private final NotebookService notebookService;

    @RequestMapping("/{notebookId}")
    public String detail(@PathVariable("notebookId") Long notebookId, Model model) {

        Notebook targetNotebook = notebookService.getNotebookById(notebookId);
        List<Notebook> notebookList = notebookService.getNotebookList();

        model.addAttribute("notebookList", notebookList);
        model.addAttribute("targetNotebook", targetNotebook);
        model.addAttribute("notePageList", targetNotebook.getNotePageList());
        model.addAttribute("targetNotePage", targetNotebook.getNotePageList().get(0));

        return "main";

    }

    @RequestMapping("/")
    public String main(Model model) {

        List<NotePage> notePageList = notePageService.getNotePageList();
        List<Notebook> notebookList = notebookService.getNotebookList();
        if (notebookList.isEmpty()) {
            return "redirect:/";
        }

        if (notePageList.isEmpty()) {
            notePageService.saveDefaultNotePage(notebookList.get(0));
            return "redirect:/";
        }

        model.addAttribute("notebookList", notebookList);
        model.addAttribute("targetNotebook", notebookList.get(0));
        model.addAttribute("notePageList", notePageList);
        model.addAttribute("targetNotePage", notePageList.get(0));

        return "main";
    }

    @PostMapping("/write")
    public String write() {
        Notebook notebook = notebookService.saveDefaultNotebook();
        notePageService.saveDefaultNotePage(notebook);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(Long notebookId) {
        if (notebookService.getNotebookList().size() > 1) {
            notebookService.deleteNotebook(notebookId);
        }
        return "redirect:/";
    }

    @PostMapping("/updateName")
    public String updateName(Long notebookId, String notebookName) {
        Notebook notebook = notebookService.getNotebookById(notebookId);
        notebookService.updateNotebook(notebook, notebookName);
        return "redirect:/";
    }
}
