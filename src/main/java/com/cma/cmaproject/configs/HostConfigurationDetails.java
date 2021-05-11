package com.cma.cmaproject.configs;
import com.cma.cmaproject.wrappers.IpAndNameWrapper;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class HostConfigurationDetails {

    public IpAndNameWrapper ipAndHostName() {
        IpAndNameWrapper ipAndNameWrapper=new IpAndNameWrapper();

        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            ipAndNameWrapper.setIp(ip.toString());
            ipAndNameWrapper.setHostName(hostname);

        } catch (UnknownHostException e) {
            e.printStackTrace();

        }
        return ipAndNameWrapper;
    }
}
