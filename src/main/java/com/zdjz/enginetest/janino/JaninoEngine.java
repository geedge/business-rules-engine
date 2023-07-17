package com.zdjz.enginetest.janino;


import com.zdjz.enginetest.People;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IExpressionEvaluator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Janino Expression表达式语法与Java一致。
 */
public class JaninoEngine {

    public static IExpressionEvaluator getJaninoEvaluator() throws Exception {
        return CompilerFactoryFactory.getDefaultCompilerFactory().newExpressionEvaluator();
    }


    /**
     * 传入变量求和
     */
    @Test
    public void rule1() throws Exception {
        IExpressionEvaluator janinoEvaluator = getJaninoEvaluator();
        //设置返回值类型,若不配置该项，则返回Object
        janinoEvaluator.setExpressionType(int.class);
        //设置参数名称及其类型
        janinoEvaluator.setParameters(
                new String[]{"a", "b", "c"},
                new Class[]{Integer.class, Integer.class, Integer.class}

        );
        String expression = "a + b + c ";
        janinoEvaluator.cook(expression);

        //给参数赋值,需要与上面定义的一一对应。
        Object[] arguments = new Object[3];
        arguments[0] = 1;
        arguments[1] = 2;
        arguments[2] = 3;
        System.out.println(janinoEvaluator.evaluate(arguments));
    }



    /**
     * 传入字符串进行空判断或值判断
     */
    @Test
    public void rule2() throws Exception {
        IExpressionEvaluator janinoEvaluator = getJaninoEvaluator();
        //设置返回值类型,若不配置该项，则返回Object
        janinoEvaluator.setExpressionType(Boolean.class);
        //设置参数名称及其类型
        janinoEvaluator.setParameters(
                new String[]{"a","b"},
                new Class[]{String.class, String.class}

        );
        //判断变量a是否为空，且变量b是否为“Str”
        //此方法不唯一，只要符合Java规范，也可以使用第三方工具类（使用import导入即可）
        String expression = "import com.zdjizhi.utils.StringUtil; StringUtil.isBlank(a) && b.equals(\"Str\")";
        janinoEvaluator.cook(expression);

        //给参数赋值,需要与上面定义的一一对应。
        Object[] arguments = new Object[2];
        arguments[1] = "Str";
        System.out.println(janinoEvaluator.evaluate(arguments));
    }


    /**
     * Janino并不提供像Aviator那种的语法糖,不支持向a.b这样的方式访问变量a中的某个属性b，仅可以像Java语言那样通过get方法进行属性的访问
     * 若变量为JavaBean类型，且JavaBean的属性为public，则也可使用a.b的方式访问
     */
    @Test
    public void rule3() throws Exception {
        IExpressionEvaluator janinoEvaluator = getJaninoEvaluator();
        //设置返回值类型,若不配置该项，则返回Object
        janinoEvaluator.setExpressionType(Boolean.class);
        //设置参数名称及其类型
        janinoEvaluator.setParameters(
                new String[]{"people"},
                new Class[]{People.class}

        );
        String expression = "people.age > 18";
        janinoEvaluator.cook(expression);
        People people = new People("xiaoming", 19, "192.168.22.11");
        //给参数赋值,需要与上面定义的一一对应。
        Object[] arguments = new Object[1];
        arguments[0] = people;
        System.out.println(janinoEvaluator.evaluate(arguments));
    }

    @Test
    public void rule4() throws Exception {
        IExpressionEvaluator janinoEvaluator = getJaninoEvaluator();
        //设置返回值类型,若不配置该项，则返回Object
        janinoEvaluator.setExpressionType(Boolean.class);
        //设置参数名称及其类型
        janinoEvaluator.setParameters(
                new String[]{"map"},
                new Class[]{Map.class}

        );
        String expression = "import com.zdjizhi.utils.IPUtil; IPUtil.getIpHostDesimal(map.get(\"server_ip\").toString()) < IPUtil.getIpHostDesimal(\"192.168.22.199\")  && IPUtil.getIpHostDesimal(map.get(\"server_ip\").toString()) > IPUtil.getIpHostDesimal(\"192.168.22.1\")";
        janinoEvaluator.cook(expression);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("server_ip", "192.168.22.200");
        hashMap.put("client_ip", "192.168.22.99");

        //给参数赋值,需要与上面定义的一一对应。
        Object[] arguments = new Object[1];
        arguments[0] = hashMap;
        System.out.println(janinoEvaluator.evaluate(arguments));
    }



}
