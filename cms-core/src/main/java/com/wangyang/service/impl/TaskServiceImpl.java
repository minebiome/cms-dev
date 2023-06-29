package com.wangyang.service.impl;

import com.wangyang.pojo.entity.Task;
import com.wangyang.common.enums.CrudType;
import com.wangyang.pojo.enums.TaskType;
import com.wangyang.common.pojo.BaseVo;
import com.wangyang.repository.TaskRepository;
import com.wangyang.service.ITaskService;
import com.wangyang.common.service.AbstractCrudService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class TaskServiceImpl extends AbstractCrudService<Task,Task, BaseVo,Integer>
        implements ITaskService {

    TaskRepository taskRepository;
    public TaskServiceImpl(TaskRepository taskRepository) {
        super(taskRepository);
        this.taskRepository=taskRepository;

    }


    @Override
    public Task findByENName(TaskType taskType, String enName){
        List<Task> tasks = taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("taskType"),taskType),
                        criteriaBuilder.equal(root.get("enName"),enName)).getRestriction();
            }
        });
        if(tasks.size()==0)return null;
        return tasks.get(0);
    }



    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
