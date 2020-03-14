package com.github.liuyehcf.framework.flow.engine.test.runtime.action;


import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.FieldCondition;

/**
 * @author hechenfeng
 * @date 2020/2/13
 */
public class FieldConditionAction extends BaseAction {

    public static String staticFieldContentWithAnnotationFalse;
    public static String staticFieldContentWithAnnotationTrue;
    public static String staticFieldContentWithoutAnnotation;

    public static String staticMethodContentWithAnnotationFalse;
    public static String staticMethodContentWithAnnotationTrue;
    public static String staticMethodContentWithoutAnnotation;

    @FieldCondition(parsePlaceHolder = false)
    private DelegateField fieldContentWithAnnotationFalse;

    @FieldCondition(parsePlaceHolder = true)
    private DelegateField fieldContentWithAnnotationTrue;

    private DelegateField fieldContentWithoutAnnotation;

    private DelegateField methodContentWithAnnotationFalse;

    private DelegateField methodContentWithAnnotationTrue;

    private DelegateField methodContentWithoutAnnotation;

    @FieldCondition(parsePlaceHolder = false)
    public void setMethodContentWithAnnotationFalse(DelegateField methodContentWithAnnotationFalse) {
        this.methodContentWithAnnotationFalse = methodContentWithAnnotationFalse;
    }

    @FieldCondition(parsePlaceHolder = true)
    public void setMethodContentWithAnnotationTrue(DelegateField methodContentWithAnnotationTrue) {
        this.methodContentWithAnnotationTrue = methodContentWithAnnotationTrue;
    }

    @Override
    public void onAction(ActionContext context) {
        staticFieldContentWithAnnotationFalse = fieldContentWithAnnotationFalse.getValue();
        staticFieldContentWithAnnotationTrue = fieldContentWithAnnotationTrue.getValue();
        staticFieldContentWithoutAnnotation = fieldContentWithoutAnnotation.getValue();

        staticMethodContentWithAnnotationFalse = methodContentWithAnnotationFalse.getValue();
        staticMethodContentWithAnnotationTrue = methodContentWithAnnotationTrue.getValue();
        staticMethodContentWithoutAnnotation = methodContentWithoutAnnotation.getValue();
    }
}