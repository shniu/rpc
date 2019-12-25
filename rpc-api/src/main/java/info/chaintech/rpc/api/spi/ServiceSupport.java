package info.chaintech.rpc.api.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceSupport {
    private static final Map<String, Object> singletonServices = new HashMap<>();

    /**
     * load 接口的实现类
     *
     * @param service 服务类
     * @param <S>     接口类型
     * @return 服务类的接口实现实例
     */
    public synchronized static <S> S load(Class<S> service) {
        ServiceLoader<S> drivers = ServiceLoader.load(service);
        return StreamSupport.stream(drivers.spliterator(), false)
                .map(ServiceSupport::singletonFilter)
                .findFirst()
                .orElseThrow(ServiceLoadException::new);
    }

    private static <S> S singletonFilter(S s) {
        if (s.getClass().isAnnotationPresent(Singleton.class)) {
            String className = s.getClass().getCanonicalName();
            Object instance = singletonServices.putIfAbsent(className, s);
            //noinspection unchecked
            return instance == null ? s : (S) instance;
        } else {
            return s;
        }
    }

    public synchronized static <S> Collection<S> loadAll(Class<S> service) {
        return StreamSupport
                .stream(ServiceLoader.load(service).spliterator(), false)
                .map(ServiceSupport::singletonFilter)
                .collect(Collectors.toList());
    }
}
