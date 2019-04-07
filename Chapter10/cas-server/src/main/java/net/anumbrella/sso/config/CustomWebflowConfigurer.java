package net.anumbrella.sso.config;

import net.anumbrella.sso.entity.CustomCredential;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.AbstractCasWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anumbrella
 */
public class CustomWebflowConfigurer extends AbstractCasWebflowConfigurer {


    public CustomWebflowConfigurer(FlowBuilderServices flowBuilderServices,
                                   FlowDefinitionRegistry flowDefinitionRegistry,
                                   ApplicationContext applicationContext,
                                   CasConfigurationProperties casProperties) {
        super(flowBuilderServices, flowDefinitionRegistry, applicationContext, casProperties);
    }


    @Override
    protected void doInitialize() {
        final Flow flow = super.getLoginFlow();
        bindCredential(flow);
    }

    /**
     * 绑定自定义的Credential信息
     *
     * @param flow
     */
    protected void bindCredential(Flow flow) {

        // 重写绑定自定义credential
        createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, CustomCredential.class);

        // 登录页绑定新参数
        final ViewState state = (ViewState) flow.getState(CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM);
        final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
        // 由于用户名以及密码已经绑定，所以只需对新加系统参数绑定即可
        // 字段名，转换器，是否必须字段
        cfg.addBinding(new BinderConfiguration.Binding("email", null, false));
        cfg.addBinding(new BinderConfiguration.Binding("telephone", null, false));
        cfg.addBinding(new BinderConfiguration.Binding("capcha", null, false));



        final ActionState actionState = (ActionState) flow.getState(CasWebflowConstants.STATE_ID_REAL_SUBMIT);
        final List<Action> currentActions = new ArrayList<>();
        actionState.getActionList().forEach(currentActions::add);
        currentActions.forEach(a -> actionState.getActionList().remove(a));

        actionState.getActionList().add(createEvaluateAction("validateLoginAction"));
        currentActions.forEach(a -> actionState.getActionList().add(a));

        actionState.getTransitionSet().add(createTransition("emailError", CasWebflowConstants.STATE_ID_INIT_LOGIN_FORM));
        actionState.getTransitionSet().add(createTransition("telephoneError", CasWebflowConstants.STATE_ID_INIT_LOGIN_FORM));
        actionState.getTransitionSet().add(createTransition("captchaError", CasWebflowConstants.STATE_ID_INIT_LOGIN_FORM));


    }
}
