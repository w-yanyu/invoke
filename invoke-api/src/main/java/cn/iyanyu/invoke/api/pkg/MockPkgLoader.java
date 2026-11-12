package cn.iyanyu.invoke.api.pkg;

import cn.iyanyu.invoke.api.config.InvokeDefinition;

public interface MockPkgLoader {
    String loadMockPkg(InvokeDefinition definition);
}