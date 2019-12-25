package info.chaintech.rpc.netty.nameservice;

import info.chaintech.rpc.api.NamingService;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

public class LocalFileNamingService implements NamingService {

    @Override
    public void connect(URI nameServiceUri) {

    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {

    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        return null;
    }

    @Override
    public Collection<String> supportedSchemes() {
        return null;
    }
}
