package com.clearent.commons.serialization;

import java.util.List;

public interface Wrapper<T> {
    
    List<T> getWrappedObject();

}
