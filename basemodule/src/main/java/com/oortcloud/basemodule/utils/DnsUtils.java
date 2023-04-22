package com.oortcloud.basemodule.utils;


import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.oortcloud.basemodule.constant.Constant.DOMAIN_ADDR;
import static com.oortcloud.basemodule.constant.Constant.SSL_DOMAIN;

public class DnsUtils {
    private final static String TAG = "zlm";
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    public static void hook() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            hookN();
        }
    }

    public static boolean isIpv4(String address) {
        return IPV4_PATTERN.matcher(address).matches();
    }

    /**
     * 7.0 之后 InetAddress 才有 impl
     */
    private static void hookN() {
        try {
            {
                LogUtils.d(TAG, "invoke 111：ipv6FirstN");
            }
            //获取InetAddress中的impl
            Field impl = InetAddress.class.getDeclaredField("impl");
            impl.setAccessible(true);
            //获取accessFlags
            Field modifiersField = Field.class.getDeclaredField("accessFlags");
            modifiersField.setAccessible(true);
            //去final
            modifiersField.setInt(impl, impl.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            //获取原始InetAddressImpl对象
            final Object originalImpl = impl.get(null);
            //构建动态代理InetAddressImpl对象
            Object dynamicImpl = Proxy.newProxyInstance(originalImpl.getClass().getClassLoader(), originalImpl.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    //如果是NETID_UNSET即是未解析ip的域名，则走这条路
                    //返回指定域名为指定地址
                    if (method.getName().equals("lookupAllHostAddr") && args != null && args.length == 2 && Integer.parseInt(args[1].toString()) == 0){
                        InetAddress[] originalAddresses = new InetAddress[1];
                        if (args[0].equals(SSL_DOMAIN)){
                            //域名与ip绑定
                            try {
                                InetAddress result = InetAddress.getByAddress(SSL_DOMAIN, DOMAIN_ADDR);
                                originalAddresses[0] = result;
                                LogUtils.d(TAG, "getByName" + " " + originalAddresses[0].toString());
                                LogUtils.d(TAG, "getByName" + " " + originalAddresses[0].getHostAddress());
                                LogUtils.d(TAG, "getByName" + " " + originalAddresses[0].getHostName());
                                LogUtils.d(TAG, "getByName" + " " + Arrays.toString(originalAddresses[0].getAddress()));

                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                            return originalAddresses;
                        }

                    }
                    //如果函数名为lookupAllHostAddr，并且参数长度为2，第一个参数是host，第二个参数是netId
                    Object originalResult = method.invoke(originalImpl, args);
                    if (method.getName().equals("lookupAllHostAddr") && args != null && args.length == 2 && originalResult != null) {
                        InetAddress[] originalAddresses = (InetAddress[]) originalResult;
                        return myDnsLogic(originalAddresses);
                    }

                    return originalResult;
                }
            });
            //替换impl为动态代理对象
            impl.set(null, dynamicImpl);
            //还原final
            modifiersField.setInt(impl, impl.getModifiers() & java.lang.reflect.Modifier.FINAL);
        } catch (Throwable e) {
            {
                LogUtils.e(TAG, "invoke 555：" + e);
            }
        }
    }


    private static InetAddress[] myDnsLogic(InetAddress[] originalAddresses) {
        // 做自己的逻辑；
        InetAddress result = null;
        LogUtils.d(TAG, "dns" + " " + originalAddresses[0].toString());
//        for (int i=0 ; i< originalAddresses.length; i++)
        if (originalAddresses != null)
        {
//            LogUtils.d(TAG, "dns" + " " + originalAddresses[0].toString());
//            LogUtils.d(TAG, "dns" + " " + originalAddresses[0].getHostAddress());
//            LogUtils.d(TAG, "dns" + " " + originalAddresses[0].getHostName());
//            LogUtils.d(TAG, "dns" + " " + Arrays.toString(originalAddresses[0].getAddress()));


            /*if (originalAddresses[0].getHostName().equals("im.oortcloud.com")){
                byte[] addr = {(byte)113,(byte)90,(byte)20,(byte)175};
//                result = InetAddress.getByAddress(addr);
                try {
                    result = InetAddress.getByAddress("im.oortcloud.com", addr);
                    originalAddresses[0] = result;

                    LogUtils.d(TAG, "dns" + " " + originalAddresses[0].toString());
                    LogUtils.d(TAG, "dns" + " " + originalAddresses[0].getHostAddress());
                    LogUtils.d(TAG, "dns" + " " + originalAddresses[0].getHostName());
                    LogUtils.d(TAG, "dns" + " " + Arrays.toString(originalAddresses[0].getAddress()));

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }*/
        }
        return originalAddresses;
    }

    /**
     * 7.0之前，不能动态代理，只能 addressCache 缓存来弄。
     * 而缓存只有 2S 有效时间
     */
    private static void hookM(final String hostName) {
        /*BackgroundExecutors.getGlobalExecutor().post(new Runnable() {
            @Override
            public void run() {
                try {
                    {
                        LogUtils.d(TAG, "invoke 111：ipv6FirstM");
                    }
                    InetAddress[] originalAddresses = InetAddress.getAllByName(hostName);
                    InetAddress[] result = myDnsLogic(originalAddresses);
                    Class inetAddressClass = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        inetAddressClass = Class.forName("java.net.Inet6AddressImpl");
                    } else {
                        inetAddressClass = InetAddress.class;
                    }
                    Field field = inetAddressClass.getDeclaredField("addressCache");
                    field.setAccessible(true);
                    Object object = field.get(inetAddressClass);
                    LogUtils.d(TAG, "test 11：" + object);
                    Class cacheClass = object.getClass();
                    Method putMethod;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //put方法在api21及以上为put(String host, int netId, InetAddress[] address)
                        putMethod = cacheClass.getDeclaredMethod("put", String.class, int.class, InetAddress[].class);
                        putMethod.setAccessible(true);
                        putMethod.invoke(object, hostName, 0, result);
                    } else {
                        //put方法在api20及以下为put(String host, InetAddress[] address)
                        putMethod = cacheClass.getDeclaredMethod("put", String.class, InetAddress[].class);
                        putMethod.setAccessible(true);
                        putMethod.invoke(object, hostName, result);
                    }
                    InetAddress[] test = InetAddress.getAllByName(hostName);
                    {
                        for (InetAddress inetAddress : test) {
                            LogUtils.d(TAG, "test 777：" + inetAddress);
                        }
                    }
                } catch (Throwable e) {
                    {
                        LogUtils.e(TAG, "invoke 666：" + e);
                    }
                }
            }
        });*/
    }
}