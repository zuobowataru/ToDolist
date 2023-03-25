package com.example.todo.app.todo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TodoForm implements Serializable {

    // 入力チェックルールをグループ化するためのインタフェースを作成
    public static interface TodoCreate {
    };

    public static interface TodoFinish {
    };
    //	    削除処理用の入力チェック
    public static interface TodoDelete {
    }

	private static final long serialVersionUID = 1L;

    //todoIdを使って削除、終了をする
    @NotNull(groups = { TodoFinish.class, TodoDelete.class })
    private String todoId;

    // (3)
    @NotNull(groups = { TodoCreate.class })
    @Size(min = 1, max = 30, groups = { TodoCreate.class })
    private String todoTitle;

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }	

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    // 追加分
 	private long viewcount;
	public long getViewcount() {
		return viewcount;
	}

	public void setViewcount(long viewcount) {
		this.viewcount = viewcount;
	}

	public long getAllcount() {
		return allcount;
	}

	public void setAllcount(long allcount) {
		this.allcount = allcount;
	}

	private long allcount;
    
}
