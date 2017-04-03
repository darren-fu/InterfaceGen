package df.open.annotation;

import com.squareup.javapoet.*;
import df.open.Data;
import df.open.Response;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by darrenfu on 17-4-1.
 */
public interface OneService {
    void doTest();

    List getList();


    public static void main(String[] args) {
        System.out.println(OneService.class.getName());
        System.out.println(OneService.class.getCanonicalName());
        System.out.println(OneService.class.getSimpleName());
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(OneService.class.getCanonicalName());
//        MethodSpec methodSpec =
        TypeName listOfHoverboards = ParameterizedTypeName.get(ClassName.get(Response.class), ParameterizedTypeName.get(Data.class,String.class));

        MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder("list").addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(listOfHoverboards);
        final TypeSpec typeSpec = builder.addMethod(methodBuilder.build())
                .build();
        final JavaFile javaFile = JavaFile.builder(OneService.class.getPackage().getName(), typeSpec)
                .indent("    ")
                .skipJavaLangImports(false)

                .build();

        final String sourceCode = javaFile.toString();
        System.out.println(sourceCode);
    }






}
