package com.zdjz.enginetest.aviatorscript;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.zdjizhi.utils.IPUtil;


import java.util.Map;

public class Ipv4StringToNum extends AbstractFunction {
    private static final Log logger = LogFactory.get();
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String ip = FunctionUtils.getStringValue(arg1, env);

        return  new AviatorBigInt(IPUtil.getIpHostDesimal(ip));
    }

    @Override
    public String getName() {
        return "Ipv4StringToNum";
    }
}
