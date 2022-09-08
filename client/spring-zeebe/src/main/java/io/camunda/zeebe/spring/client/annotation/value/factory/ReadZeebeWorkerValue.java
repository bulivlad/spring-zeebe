package io.camunda.zeebe.spring.client.annotation.value.factory;

import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.camunda.zeebe.spring.client.annotation.value.ZeebeWorkerValue;
import io.camunda.zeebe.spring.client.bean.MethodInfo;
import io.camunda.zeebe.spring.client.bean.ParameterInfo;
import io.camunda.zeebe.spring.util.ZeebeExpressionResolver;

import java.util.List;
import java.util.Optional;

public class ReadZeebeWorkerValue extends ReadAnnotationValue<MethodInfo, ZeebeWorker, ZeebeWorkerValue> {

  public ReadZeebeWorkerValue(final ZeebeExpressionResolver resolver) {
    super(resolver, ZeebeWorker.class);
  }

  @Override
  public Optional<ZeebeWorkerValue> apply(final MethodInfo methodInfo) {
    return methodInfo
      .getAnnotation(annotationType)
      .map(
        annotation -> {
          ZeebeWorkerValue.ZeebeWorkerValueBuilder builder = ZeebeWorkerValue.builder()
            .methodInfo(methodInfo)
            .type(resolver.resolve(annotation.type()))
            .timeout(annotation.timeout())
            .maxJobsActive(annotation.maxJobsActive())
            .pollInterval(annotation.pollInterval())
            .fetchVariables(annotation.fetchVariables())
            .forceFetchAllVariables(annotation.forceFetchAllVariables())
            .autoComplete(annotation.autoComplete())
            .variableParameters(readZeebeVariableParameters(methodInfo))
            .requestTimeout(annotation.requestTimeout())
            .enabled(annotation.enabled());

          String name = resolver.resolve(annotation.name());
          if (name != null && name.length() > 0) {
            builder.name(name);
          }

          return builder.build();
        });
  }

  private List<ParameterInfo> readZeebeVariableParameters(MethodInfo methodInfo) {
    return methodInfo.getParametersFilteredByAnnotation(ZeebeVariable.class);
  }
}
