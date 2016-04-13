package zeriff.com.mhub.common;

import java.io.OptionalDataException;

/**
 * Created by SUJAN on 4/1/2016.
 */
public interface IEventCallBack {

    void OnSuccess(Object obj);
    void OnFailure(Object obj);
}
