package df.open.annotation;

import com.squareup.javapoet.TypeName;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

/**
 * 说明:
 * <p/>
 * Copyright: Copyright (c)
 * <p/>
 * Company:
 * <p/>
 *
 * @author 付亮(OF2101)
 * @version 1.0.0
 * @date 2016/10/11
 */
//生成接口的处理类 ，在此不考虑方法的参数及返回类型(为了演示简单)
@SupportedAnnotationTypes("df.open.annotation.GenerateImpl")
//@SupportedOptions({"name"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)

public class GenerateImplProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    private int r = 1;//轮循次数

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        //初始化Filer和Messager
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "process() is execute...");
        //获取所有编译类元素，并打印，测试用
        Set<? extends Element> elements = roundEnv.getRootElements();
        System.out.println("输入的所有类有：");
        for (Element e : elements) {
            System.out.println(">>> " + e.getSimpleName());
        }

        //获取使用了注解@GenerateInterface的类元素
        System.out.println("需要生成相应接口的类有：");
        Set<? extends Element> genElements = roundEnv
                .getElementsAnnotatedWith(GenerateImpl.class);
        for (Element e : genElements) {
            System.out.println(">>> " + e.getSimpleName() + ",element :" + e);
            GenerateImpl gi = e.getAnnotation(GenerateImpl.class);
            String className = e.getSimpleName() + gi.suffix();
            String pkgPath =e.getEnclosingElement().toString();// e.getClass().getCanonicalName().replaceFirst("\\." + e.getSimpleName() + "$", "");
            String classString =
                    "package " + pkgPath + ";\n"
                            + "public class " + className + " implements " + e.getSimpleName() + "{\n";
            //获取所有的方法元素
            List<? extends Element> genElementAlls = e.getEnclosedElements();
            System.out.println(">>>> 类" + e.getSimpleName()
                    + "封装元素(仅对修饰符有public的生成接口方法):");
            for (Element e1 : genElementAlls) {
                System.out.println(">>> >>> " + e1.getSimpleName()
                        + " 修饰符：" + e1.getModifiers());
                System.out.println(">>> >>> TypeName: " + TypeName.get(((ExecutableElement) e1).getReturnType()));


                if (!e1.getSimpleName().toString().equals("<init>")
                        && e1.asType() instanceof ExecutableType && isPublic(e1)) {
                    System.out.println(">>> >>> >>> " + e1.getSimpleName());
                    classString += "public  void " + e1.getSimpleName() + "() {\n" + " System.out.println(\"自动生成实现类\");}";
                }
            }
            classString += "}";
            //System.out.println(classString);
            try {
                JavaFileObject jfo = filer
                        .createSourceFile(pkgPath + "." + className, e);
                Writer writer = jfo.openWriter();
                writer.flush();
                writer.append(classString);
                writer.flush();
                writer.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("-------------------注解处理器第" + (r++) + "次循环处理结束...\n");
        return true;
    }

    //判断元素是否为public
    public boolean isPublic(Element e) {
        //获取元素的修饰符Modifier,注意此处的Modifier
        //非java.lang.reflect.Modifier
        Set<Modifier> modifiers = e.getModifiers();
        for (Modifier m : modifiers) {
            if (m.equals(Modifier.PUBLIC)) return true;
        }
        return false;
    }

}
