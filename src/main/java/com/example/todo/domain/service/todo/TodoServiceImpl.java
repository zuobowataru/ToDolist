package com.example.todo.domain.service.todo;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoRepository;

@Service
@Transactional 
public class TodoServiceImpl implements TodoService {

    private static final long MAX_UNFINISHED_COUNT = 5;

    @Inject// TodoRepositoryの実装をインジェクション
    TodoRepository todoRepository;

    @Override
    @Transactional(readOnly = true) // 参照のみ行う処理に関しては、readOnly=trueをつける。の最適化が行われ
    public Collection<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo create(Todo todo) {
        long unfinishedCount = todoRepository.countByFinished(false);
        if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
            // (5)
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage
                    .fromText("[E001] The count of un-finished Todo must not be over "
                            + MAX_UNFINISHED_COUNT + "."));
            // (6)
            throw new BusinessException(messages);
        }

        // (7)
        String todoId = UUID.randomUUID().toString();
        Date createdAt = new Date();

        todo.setTodoId(todoId);
        todo.setCreatedAt(createdAt);
        todo.setFinished(false);

        todoRepository.create(todo);
        /* REMOVE THIS LINE IF YOU USE JPA
            todoRepository.save(todo); // (8)
           REMOVE THIS LINE IF YOU USE JPA */

        return todo;
    }

    @Override
    public Todo finish(String todoId) {
        Todo todo = findOne(todoId);
        if (todo.isFinished()) {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage
                    .fromText("[E002] The requested Todo is already finished. (id="
                            + todoId + ")"));
            throw new BusinessException(messages);
        }
        todo.setFinished(true);
        todoRepository.update(todo);
        /* REMOVE THIS LINE IF YOU USE JPA
            todoRepository.save(todo); // (9)
           REMOVE THIS LINE IF YOU USE JPA */
        return todo;
    }

    @Override
    public void delete(String todoId) {
        Todo todo = findOne(todoId);
        todoRepository.delete(todo);
    }

    // (10)
    private Todo findOne(String todoId) {
        // (11)
        return todoRepository.findById(todoId).orElseThrow(() -> {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage
                    .fromText("[E404] The requested Todo is not found. (id="
                            + todoId + ")"));
            return new ResourceNotFoundException(messages);
        });
    }
    // 追加分
    @Override
    @Transactional(readOnly = true) 
	public Todo findView(){
    	Todo todo = new Todo(); 
    	long viewCount = todoRepository.findView();
    	long unfinishedCount = todoRepository.countByFinished(false);
    	
    	todo.setViewcount(viewCount);   	
    	todo.setAllcount(unfinishedCount);
    	return todo;
    }
    
}

