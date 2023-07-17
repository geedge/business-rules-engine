package com.zdjz.enginetest.aviatorscript;


import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.zdjz.enginetest.People;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;


/**
 * AviatorScript文档请参考：https://www.yuque.com/boyan-avfmj/aviatorscript/lvabnw
 */
public class AviatorScriptEngine {

    public static AviatorEvaluatorInstance getAviatorEvaluator() {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        instance.setCachedExpressionByDefault(true);
        instance.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
        instance.setFunctionMissing(null);
        return instance;
    }

    /**
     * 传入变量求和
     */
    @Test
    public void rule1() {
        String expression = "a + b + c ";
        Expression compiledExp = getAviatorEvaluator().compile(expression, true);
        System.out.println(compiledExp.execute(compiledExp.newEnv("a", 1, "b", 2, "c", 3)));
    }


    /**
     * 传入字符串进行空判断或值判断
     * nil
     * 当我们想表示一个变量还没有赋值的时候，需要用到 nil  这个特殊类型，它和 java 中的 null  作用一样，表示当前变量还没有赋值。nil 最常见的场景就是用于判断变量是否为 null：
     */
    @Test
    public void rule2() {
        String expression = "a == nil  && b == 'Str'";
        Expression compiledExp = getAviatorEvaluator().compile(expression, true);
        System.out.println(compiledExp.execute(compiledExp.newEnv("b", "Str")));
    }

    /**
     * Aviator 有个方便用户使用变量的语法糖, 当你要访问变量a中的某个属性b, 那么你可以通过a.b访问到, 更进一步, a.b.c将访问变量a的b属性中的c属性值, 推广开来也就是说 Aviator 可以将变量声明为嵌套访问的形式。
     * TestAviator类符合JavaBean规范, 并且是 public的，例如：Map或POJO等。（具体测试时，属性为private也可以被访问到）
     */
    @Test
    public void rule3() {
        AviatorEvaluatorInstance aviatorEvaluator = getAviatorEvaluator();

        //当变量是POJO类时，且使用系统自带函数进行判断
        //当前支持的函数库列表：https://www.yuque.com/boyan-avfmj/aviatorscript/ashevw
        //判断这个人的是否姓xiao且年龄大于18岁
        String expression1 = "string.startsWith(people.name,'xiao') && people.age > 18 ";
        Expression compiledExp1 = getAviatorEvaluator().compile(expression1, true);
        People people = new People("xiaoming", 19, "192.168.22.11");
        System.out.println(compiledExp1.execute(compiledExp1.newEnv("people", people)));

        //当变量为map且使用自定义函数进行判断
        //添加自定义函数，更多的自定义函数方式请参考：https://www.yuque.com/boyan-avfmj/aviatorscript/gpyg6p
        //判断server_ip是否在192.168.22.1和192.168.22.199之间
        aviatorEvaluator.addFunction(new Ipv4StringToNum());
        String expression2 = "Ipv4StringToNum(map.server_ip) < Ipv4StringToNum('192.168.22.199') &&Ipv4StringToNum(map.server_ip) > Ipv4StringToNum('192.168.22.1') ";
        Expression compiledExp2 = aviatorEvaluator.compile(expression2, true);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("server_ip", "192.168.22.200");
        hashMap.put("client_ip", "192.168.22.99");
        System.out.println(compiledExp2.execute(compiledExp2.newEnv("map", hashMap)));
    }


    /**
     * AviatorScript 支持多行表达式，表达式之间必须以分号 ; 隔开,支持换行。
     */
    @Test
    public void rule4() {
        AviatorEvaluatorInstance aviatorEvaluator = getAviatorEvaluator();

        //使用let定义变量进行处理
        String expression1 = "let a = 1;  let b = 2;  c = a + b; ";
        Expression compiledExp1 = aviatorEvaluator.compile(expression1, true);
        System.out.println(compiledExp1.execute());

        //整个脚本的返回结果默认是最后一个表达式的结果。但是这里需要注意的是，加上分号后，整个表达式的结果将固定为 nil，因此如果你执行上面的脚本，并打印结果，一定是 null，而不是 c 的值：
        //如果你想返回表达式的值，而不是为 nil，最后一个表达式不加分号即可:
        String expression2 = "let a = 1;  let b = 2;  c = a + b ";
        Expression compiledExp2 = aviatorEvaluator.compile(expression2, true);
        System.out.println(compiledExp2.execute());

        //也可以用 return 语句来指定返回,return 也用于提前返回，结合条件语句可以做更复杂的逻辑判断。
        //注意， return 语句就必须加上分号才是完整的一条语句，否则将报语法错误。
        //使用条件语句进行判断,更多用法查看：https://www.yuque.com/boyan-avfmj/aviatorscript/mcoghv
        String expression3 = "if (people.name == 'xiaomig') { return '这是小明';}  return '这不是小明'; ";
        Expression compiledExp3 = aviatorEvaluator.compile(expression3, true);
        People people = new People("xiaoming", 19, "192.168.22.11");
        System.out.println(compiledExp3.execute(compiledExp3.newEnv("people", people)));
    }


    /**
     * AviatorScript 支持 for 和 while 两种循环语句，和其他语言一样也支持return、break、continue、多层嵌套等。
     * 更多用法查看：https://www.yuque.com/boyan-avfmj/aviatorscript/vfqgqn
     */
    @Test
    public void rule5() {
        AviatorEvaluatorInstance aviatorEvaluator = getAviatorEvaluator();

        String expression1 = "for i in range(0, 10) {println(i)} ";
        Expression compiledExp1 = aviatorEvaluator.compile(expression1, true);
        System.out.println("for循环：");
        compiledExp1.execute();

        String expression2 = "let sum = 1; while sum < 1000 { sum = sum + sum; }  println(sum);";
        Expression compiledExp2 = aviatorEvaluator.compile(expression2, true);
        System.out.println("while循环：");
        compiledExp2.execute();
    }


    /**
     * AviatorScript 完整支持了 java 的异常处理机制，只是做了一些简化,
     * 更多用法查看：https://www.yuque.com/boyan-avfmj/aviatorscript/sikgal
     * 1. throw 抛出了一个字符串，在 AviatorScript 中，可以 throw 任何东西，非异常的对象都将被转化包装为标准错误 com.googlecode.aviator.exception.StandardError 类的实例。
     * 2. catch(e) 没有指定异常的类型， AviatorScript 允许不指定异常类型，等价于  catch(Throwable e) 。
     * 3. pst(e)  用于打印异常堆栈，也就是 e.printStackTrace() 调用。
     * 4. AviatorScript 中同样支持 finally 语句，这跟 Java 保持一致。
     */
    @Test
    public void rule6() {
        AviatorEvaluatorInstance aviatorEvaluator = getAviatorEvaluator();
        String expression = "try { throw \"an exception\";  } catch(e) { pst(e);  } finally {  p(\"finally\");}";
        Expression compiledExp = aviatorEvaluator.compile(expression, true);
        compiledExp.execute();
    }


    /**
     * AviatorScript 支持编译脚本，脚本语法与expression表达式相同
     * @throws IOException
     */
    @Test
    public void rule7() throws IOException {
        //编译脚本
        //路径是文件系统的绝对路径或相对路径，
        //相对路径的时候，必须项目的根目录开始的相对路径
        //classpath下的绝对或相对路径
        //脚本内容：let sum = 1; while sum < 1000 { sum = sum + sum; }  println(sum);
        Expression compiledExp = AviatorEvaluator.getInstance().compileScript("src/main/java/com/zdjz/enginetest/aviatorscript/script.av");
        compiledExp.execute();

    }


}
