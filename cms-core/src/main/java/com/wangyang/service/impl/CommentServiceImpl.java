package com.wangyang.service.impl;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.exception.OptionException;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.dto.CommentDto;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Comment;
import com.wangyang.common.pojo.BaseEntity;
import com.wangyang.common.enums.CrudType;
import com.wangyang.pojo.vo.CommentVo;
import com.wangyang.service.authorize.IUserService;
import com.wangyang.repository.CommentRepository;
import com.wangyang.service.IArticleService;
import com.wangyang.service.ICommentService;
import com.wangyang.common.service.AbstractCrudService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl
        extends AbstractCrudService<Comment, BaseEntity, CommentVo,Integer> implements ICommentService {
//    @Autowired
//    UserRepository userRepository;
//    @Autowired

    @Autowired
    IArticleService articleService;
    @Autowired
    IUserService userService;
    private CommentRepository commentRepository;
    public CommentServiceImpl(CommentRepository commentRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment add(Comment comment) {
        if(StringUtils.isEmpty(comment.getUsername())&&comment.getUserId()!=null){
//            Optional<User> user = userService.findOptionalBy(comment.getUserId());
//            if(!user.isPresent()){
//                throw new ObjectException("用户对象没有找到,不存在不能添加评论!");
//            }
//            comment.setUsername(user.get().getUsername());
//            comment.setEmail(user.get().getEmail());
        }

        Article article = articleService.findArticleById(comment.getArticleId());
        if(!article.getOpenComment()){
            throw new OptionException("文章没有打开评论,不能添加!");
        }
        commentRepository.save(comment);
        articleService.updateCommentNum(comment.getArticleId(),1);
//        Optional<User> userOptional = userRepository.findById(uid);

//        Optional<Article> articleOptional = articleRepository.findById(aid);
        return comment;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }

    @Override
    public void deleteById(int id) {
        Comment comment = findById(id);
        commentRepository.deleteById(id);
        articleService.updateCommentNum(comment.getArticleId(),-1);
    }

    @Override
    public Comment update(int id, Comment updateComment) {
        return null;
    }

    @Override
    public List<CommentVo> listVoBy(int articleId){
        //TODO 这里需要从数据库设置
        List<Comment> comments = listBy(articleId);
        List<CommentVo> voList = convertTo(comments);
        List<CommentVo> commentVos = listWithTree(voList);
        return commentVos;
    }

    @Override
    public Page<CommentVo> pageVoBy(int articleId, Pageable pageable){
        return convertTo(pageBy(articleId,pageable));
    }

    public Page<CommentVo> convertTo(Page<Comment> commentPage){
        return  commentPage.map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment,commentVo);
            return commentVo;
        });
    }

    public List<CommentVo> convertTo(List<Comment> list){
        Set<Integer> userIds = ServiceUtil.fetchProperty(list, Comment::getUserId);
        List<User> users = userService.findAllById(userIds);
        Map<Integer, User> userMap = ServiceUtil.convertToMap(users, User::getId);

        Map<Integer, Comment> commentMap = ServiceUtil.convertToMap(list, Comment::getId);

        List<CommentVo> commentVos = list.stream().map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            User user = userMap.get(comment.getUserId());
            if(user==null){
                user = new User();
                user.setUsername("删除用户");
            }
            commentVo.setUser(user);
            if(comment.getParentId()!=0){
                Comment parentComment= commentMap.get(comment.getParentId());
                if(parentComment==null){
                    throw new ObjectException(comment.getId()+"的父类不能找到！！");
                }
                commentVo.setReplyUser(userMap.get(parentComment.getUserId()));
            }
            return commentVo;
        }).collect(Collectors.toList());
        return commentVos;
    }


    @Override
    public Page<Comment> pageBy(int articleId, Pageable pageable){
//        Comment comment = new Comment();
//        comment.setResourceId(id);
//        comment.setCommentType(commentType);
        Specification<Comment> specification = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("articleId"),articleId)
                                           ).getRestriction();
            }
        };

        return commentRepository.findAll(specification,pageable);
    }

    public List<Comment> listBy(int articleId){
//        Comment comment = new Comment();
//        comment.setResourceId(id);
//        comment.setCommentType(commentType);
        Specification<Comment> specification = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("articleId"),articleId)
                ).getRestriction();
            }
        };
        return commentRepository.findAll(specification,Sort.by(Sort.Order.asc("id")));
    }

    @Override
    public Page<CommentDto> pageDtoBy(int articleId, Pageable pageable) {
        return pageBy(articleId,pageable).map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            return commentDto;
        });
    }




    @Override
    public Comment findById(int id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent()){
            throw  new ObjectException("Comment 对象不存在！！");
        }
        return comment.get();
    }



    @Override
    public Page<Comment> list(Pageable pageable) {
        return null;
    }
}
