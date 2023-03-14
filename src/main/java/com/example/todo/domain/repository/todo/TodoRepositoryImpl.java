package com.example.todo.domain.repository.todo;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.todo.domain.model.Todo;


@Repository // (1)
public class TodoRepositoryImpl implements TodoRepository {
    private static final Map<String, Todo> TODO_MAP = new ConcurrentHashMap<String, Todo>();
 
    static {
        Todo todo1 = new Todo();
        todo1.setTodoId("1");
        todo1.setTodoTitle("Send a e-mail");
        Todo todo2 = new Todo();
        todo2.setTodoId("2");
        todo2.setTodoTitle("Have a lunch");
        Todo todo3 = new Todo();
        todo3.setTodoId("3");
        todo3.setTodoTitle("Read a book");
        todo3.setFinished(true);
        TODO_MAP.put(todo1.getTodoId(), todo1);
        TODO_MAP.put(todo2.getTodoId(), todo2);
        TODO_MAP.put(todo3.getTodoId(), todo3);
    }    
    @Override
    public Optional<Todo> findById(String todoId) {
        return Optional.ofNullable(TODO_MAP.get(todoId));
    }

    @Override
    public Collection<Todo> findAll() {
        return TODO_MAP.values();
    }

    @Override
    public void create(Todo todo) {
        TODO_MAP.put(todo.getTodoId(), todo);
    }

    @Override
    public boolean update(Todo todo) {
        TODO_MAP.put(todo.getTodoId(), todo);
        return true;
    }

    @Override
    public void delete(Todo todo) {
        TODO_MAP.remove(todo.getTodoId());
    }

    @Override
    public long countByFinished(boolean finished) {
        long count = 0;
        for (Todo todo : TODO_MAP.values()) {
            if (finished == todo.isFinished()) {
                count++;
            }
        }
        return count;
    }
    
}