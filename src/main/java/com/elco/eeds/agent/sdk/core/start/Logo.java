package com.elco.eeds.agent.sdk.core.start;

/**
 * @author ：ytl
 * @date ：Created in 2022/12/20 11:08
 * @description：
 */
public class Logo {
    public static String logo = null;

    static {

        logo = " ________  __                  \n" +
                "|_   __  |[  |                 \n" +
                "  | |_ \\_| | |  .---.   .--.   \n" +
                "  |  _| _  | | / /'`\\]/ .'`\\ \\ \n" +
                " _| |__/ | | | | \\__. | \\__. | \n" +
                "|________|[___]'.___.' '.__.'  \n" +
                "客户端SDK启动成功                 ";

    }

//    private static String version;
//
//    public static String getVersion() throws IOException {
////        String res = "META-INF/maven/com.elco/eeds-agent-sdk/pom.properties"
//
//
////        String path = "META-INF/maven/cn.hutool/hutool-all/pom.properties";
//
////        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
//
//        if (version == null) {
//
//            String res = "META-INF/maven/com.elco/eeds-agent-sdk/pom.properties";
//            ClassPathResource resource = new ClassPathResource(res);
//
//            URL url = Thread.currentThread().getContextClassLoader().getResource(res);
//            if (url == null) {
//
//                version = "未知" + ".SNAPSHOT";
//
//            } else {
//
//                Properties properties = new Properties();
//
//                byte[] bytes = getFileFromJar(resource.getFile(), "pom.properties");
//                properties.load(new ByteArrayInputStream(bytes));
//                //pomPropertiesCache = properties;
////                logger.info("{} 读取到pom.properties的内容:{}", file.getName(), properties);
//                version = (String) properties.get("version");
//
////                version=  PropertiesUtil.getValue( url.getFile(),"version");
////                Properties props = Utils.loadProperties(res);
////
////                version = props.getProperty(“version”);
//
//            }
//
//        }
//
//        return version;
//
//    }
//
//    public static void main(String[] args) throws IOException {
//        getVersion();
//    }
//
//
//    private static byte[] getFileFromJar(File file, String filename) {
//        try (JarFile jarFile = new JarFile(file)) {
//            Enumeration<JarEntry> entries = jarFile.entries();
//            while (entries.hasMoreElements()) {
//                JarEntry jarEntry = entries.nextElement();
//                if (jarEntry.getName().endsWith(filename)) {
//                    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
//                        byte[] bytes = new byte[inputStream.available()];
//                        inputStream.read(bytes);
//                        return bytes;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
////        logger.warn("从 {} 中获取 {} 失败", file.getName(), filename);
//        throw new RuntimeException(String.format("从 %s 中获取 %s 失败", file.getName(), filename));
//    }
}
