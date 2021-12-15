package edu.common.dynamicextensions.nutility;

public class FormProperties {

    private static FormProperties instance = null;

    private FormPostProcessor postProcessor;

    protected FormProperties() {
            // Exists only to defeat instantiation.
    }

    public static FormProperties getInstance() {
            if (instance == null) {
                    instance = new FormProperties();
            }
            return instance;
    }

    public FormPostProcessor getPostProcessor() {
            return postProcessor;
    }

    public void setPostProcessor(FormPostProcessor postProcessor) {
            this.postProcessor = postProcessor;
    }

}