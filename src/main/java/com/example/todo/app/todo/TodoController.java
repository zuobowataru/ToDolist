package com.example.todo.app.todo;

import java.util.Collection;

import javax.inject.Inject;
import javax.validation.groups.Default;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.todo.app.todo.TodoForm.TodoCreate;
import com.example.todo.app.todo.TodoForm.TodoDelete;
import com.example.todo.app.todo.TodoForm.TodoFinish;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.service.todo.TodoService;
import com.github.dozermapper.core.Mapper;

@Controller // (1)
@RequestMapping("todo") // (2)
public class TodoController {
    @Inject // (1)
    TodoService todoService;

    // (1)
    @Inject
    Mapper beanMapper;    
    
    @ModelAttribute // FORMの初期化
    public TodoForm setUpForm() {
        TodoForm form = new TodoForm();
        return form;
    }
    

    
    @GetMapping("list") // todo/list　パスにGETでリクエストした際に、一覧画面表示が実行。 @GetMappingで画面表示
    public String list(Model model) {
        Collection<Todo> todos = todoService.findAll();
       Todo todos2 = todoService.findView();
        
        model.addAttribute("todos", todos); // modelにToDoListを追加して、VIEWに渡す

        // 今回追加した値を設定する
        model.addAttribute("title", "オリジナルタイトルだよ");        
        model.addAttribute("viewcount", todos2.getViewcount());
        model.addAttribute("allcount", todos2.getAllcount());
        
        return "todo/list"; // VIEW名に"todo/list"を渡すとspringーmvc.xmlによってJSPがレンダリングされる。
    }
    
    @PostMapping("create") // todo/create　パスにPOSTでリクエストした際に、新規作成処理が実行
    public String create(
    		@Validated({ Default.class, TodoCreate.class }) TodoForm todoForm, // グループ化した入力チェックルールを適用するため@Validated
    		BindingResult bindingResult, // フォームの入力チェックのため、  @Validatedを実施。入力チェック結果を 	bindingResultに格納。	
            Model model, RedirectAttributes attributes) { // 正常完了後、リダイレクトして一覧画面表示。

        // (5)
        if (bindingResult.hasErrors()) {
            return list(model);
        }

        // (6)
        Todo todo = beanMapper.map(todoForm, Todo.class);

        try {
            todoService.create(todo);
        } catch (BusinessException e) {
            // (7)
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        // (8)
        attributes.addFlashAttribute(ResultMessages.success().add(
                ResultMessage.fromText("Created successfully!")));

     
        return "redirect:/todo/list";
    }
    // /todo/finishというパスにPOSTメソッドを使用してリクエストされた際に、完了処理用のメソッド(finishメソッド)が実行される
    @PostMapping("finish") 
    public String finish(
            @Validated({ Default.class, TodoFinish.class }) TodoForm form, // (3)
            BindingResult bindingResult, Model model,
            RedirectAttributes attributes) {
        // 入力エラーがあった場合、一覧画面に戻る。
        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.finish(form.getTodoId());
        } catch (BusinessException e) {
            // BusinessExceptionが発生した場合は、結果メッセージをModelに追加して、一覧画面に戻る。
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        // 結果メッセージをflashスコープに追加して、一覧画面でリダイレクトする。
        attributes.addFlashAttribute(ResultMessages.success().add(
                ResultMessage.fromText("Finished successfully!")));
        return "redirect:/todo/list";
    }    
    @PostMapping("delete") //  削除処理用のメソッド
    public String delete(
            @Validated({ Default.class, TodoDelete.class }) TodoForm form,
            BindingResult bindingResult, Model model,
            RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.delete(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        attributes.addFlashAttribute(ResultMessages.success().add(
                ResultMessage.fromText("Deleted successfully!")));
        return "redirect:/todo/list";
    }

}