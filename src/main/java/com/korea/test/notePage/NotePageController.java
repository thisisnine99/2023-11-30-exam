package com.korea.test.notePage;

import com.korea.test.notebook.Notebook;
import com.korea.test.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotePageController {

    private final NotePageService notePageService;
    private final NotebookService notebookService;

    @RequestMapping("/test")
    @ResponseBody public String test() {
        return "test";
    }

    @RequestMapping("/")
    public String main(Model model) {
        List<Notebook> notebookList = notebookService.getNotebookList();

        if(notebookList.isEmpty()) {
            notebookService.saveDefaultNotebook();
            return "redirect:/";
        }

        Notebook targetNotebook = notebookList.get(0);
        List<NotePage> notePageList = notePageService.getNotePageListByNotebook(targetNotebook);

        if(notePageList.isEmpty()) {
            notePageService.saveDefaultNotePage(targetNotebook);
            return "redirect:/";
        }

        //2. 꺼내온 데이터를 템플릿으로 보내기
        model.addAttribute("notebookList", notebookList);
        model.addAttribute("targetNotebook", targetNotebook);
        model.addAttribute("notePageList", notePageList);
        model.addAttribute("targetNotePage", notePageList.get(0));

        return "main";
    }

    @PostMapping("/write")
    public String write(Long notebookId) {
        Notebook notebook = notebookService.getNotebookById(notebookId);
        notePageService.saveDefaultNotePage(notebook);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        NotePage targetNotePage = notePageService.getNotePageById(id);
        List<NotePage> notePageList = notePageService.getNotePageListByNotebook(targetNotePage.getNotebook());
        List<Notebook> notebookList = notebookService.getNotebookList();

        model.addAttribute("targetNotePage", targetNotePage);
        model.addAttribute("notePageList", notePageList);
        model.addAttribute("notebookList", notebookList);
        model.addAttribute("targetNotebook", targetNotePage.getNotebook());

        return "main";
    }
    @PostMapping("/update")
    public String update(Long id, String title, String content) {
        NotePage notePage = notePageService.getNotePageById(id);

        if(title.trim().length() == 0) {
            title = "제목 없음";
        }

        notePage.setTitle(title);
        notePage.setContent(content);

        notePageService.save(notePage);
        return "redirect:/detail/" + id;
    }

    @PostMapping("/delete")
    public String delete(Long id) {
        notePageService.deleteById(id);
        return "redirect:/";
    }

}
