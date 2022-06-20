package com.graphqlexample.project.aop.aspects;

import com.graphqlexample.project.exceptions.ResourceNotFoundException;
import com.graphqlexample.project.models.dtos.CommentUpdateDto;
import com.graphqlexample.project.repositories.CommentRepository;
import com.graphqlexample.project.services.implementations.AuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommentAspect {

    private final CommentRepository commentRepository;

    private final AuthServiceImpl authService;

    @Pointcut("@annotation(com.graphqlexample.project.aop.annotations.MethodLogger)")
    public void annotationPointCut() {}

    @Before("annotationPointCut()")
    public void beforeLogger(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)
                joinPoint.getSignature();
        Method method = signature.getMethod();
        String args = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        log.info("{} is started working", method.getName());
        log.info("before " + joinPoint.toString() + ", args=[" + args + "]");
    }

    @After("annotationPointCut()")
    public void afterLogger(JoinPoint joinPoint) {
        log.info("after " + joinPoint.toString());
    }

    @Before("@annotation(com.graphqlexample.project.aop.annotations.CommentSecurity) && args(updateDto,..)")
    public void updateCommentSecurity(JoinPoint jp, CommentUpdateDto updateDto) {
        var comment = commentRepository.findById(updateDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + updateDto.getId() + " not found!"));
        if (!authService.isOwnerUser(comment.getUser()) && !authService.isAdminUser()) {
            throw new AccessDeniedException("This user has no permision to update comment!");
        }
    }

    @Before("@annotation(com.graphqlexample.project.aop.annotations.CommentSecurity) && args(id,..)")
    public void deleteCommentSecurity(JoinPoint jp, Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found!"));
        if (!authService.isOwnerUser(comment.getUser()) && !authService.isAdminUser()) {
            throw new AccessDeniedException("This user has no permision to update comment!");
        }
    }
}