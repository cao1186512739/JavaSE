package com.zjzy.MVC.extspringmvc.ext.servet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zjzy.MVC.extspringmvc.extannotation.ExtController;
import com.zjzy.MVC.extspringmvc.extannotation.ExtRequestMapping;
import com.zjzy.MVC.extspringmvc.utils.ClassUtil;
import org.apache.commons.lang.StringUtils;


/**
 * @QQ644064779 1.自定义DispatcherServlet<br>
 * 2.servlet init()方法初始化###只会执行一次<br>
 * ######2.1获取当前包下所有的类<br>
 * ######2.2初始化当前包下所有的类,使用Java反射机制初始化对象存放在SpringMVC容器中key(beanId)-
 * value( 当前实例对象) <br>
 * ######2.3初始化HandlerMapping方法,将url和方法对应上 <br>
 * ########2.3.1使用Java反射技术读取类的信息,存放在map集合中key为url请求地址,value为对应方法
 * <br>
 * ########2.3.2使用Java反射技术读取类的信息,存放在map集合中key为url请求地址,value为对应实例对象
 * <br>
 * 3.servlet get或者post请求<br>
 * ######## 3.1.1获取请求地址,使用Java反射技术找到对应的方法和实例对象进行执行 <br>
 * 作者: 每特教育-余胜军<br>
 * 联系方式:QQ644064779|WWW.itmayiedu.com<br>
 */
public class ExtDispatcherServlet extends HttpServlet {
    // mvc bean key=beanid ,value=对象
    private ConcurrentHashMap<String, Object> mvcBeans = new ConcurrentHashMap<String, Object>();
    // mvc 请求方法 key=requestUrl,value=对象
    private ConcurrentHashMap<String, Object> mvcBeanUrl = new ConcurrentHashMap<String, Object>();
    // mvc 请求方法 key=requestUrl,value=方法
    private ConcurrentHashMap<String, String> mvcMethodUrl = new ConcurrentHashMap<String, String>();

    /**
     * 初始化自定义SpringMVC容器
     */
    public void init() throws ServletException {
        try {
            // 1.获取当前包下所有的类
            List<Class<?>> classes = ClassUtil.getClasses("com.zjzy.MVC.ext.controller");
            // 2.初始化当前包下所有的类,使用Java反射机制初始化对象存放在SpringMVC容器中key(beanId)-value(
            // 当前实例对象)
            findClassMVCBeans(classes);
            // 3.初始化HandlerMapping方法,将url和方法对应上
            handlerMapping(mvcBeans);
        } catch (Exception e) {

        }
    }

    // 2.初始化当前包下所有的类,使用Java反射机制初始化对象存放在SpringMVC容器中key(beanId)-value(
    // 当前实例对象)
    public void findClassMVCBeans(List<Class<?>> classes)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        mvcBeans = new ConcurrentHashMap<String, Object>();
        for (Class<?> classInfo : classes) {
            ExtController extController = classInfo.getDeclaredAnnotation(ExtController.class);
            if (extController != null) {
                // 默认类名小写 作为bean的名称
                String beanId = ClassUtil.toLowerCaseFirstOne(classInfo.getSimpleName());
                mvcBeans.put(beanId, ClassUtil.newInstance(classInfo));
            }
        }

    }

    // 3.初始化HandlerMapping方法,将url和方法对应上
    public void handlerMapping(ConcurrentHashMap<String, Object> mvcBeans) {
        // 遍历mvc bean对象
        for (Map.Entry<String, Object> entry : mvcBeans.entrySet()) {
            // springmvc 注入object对象
            Object mvcObject = entry.getValue();
            // 判断类上是否有@ExtRequestMapping注解
            Class<? extends Object> classInfo = mvcObject.getClass();
            String requestBaseUrl = null;
            ExtRequestMapping classExtRequestMapping = classInfo.getAnnotation(ExtRequestMapping.class);
            if (classExtRequestMapping != null) {
                requestBaseUrl = classExtRequestMapping.value();
            }
            // 遍历当前类的所有方法,判断方法上是否有注解
            Method[] declaredMethods = classInfo.getDeclaredMethods();
            for (Method method : declaredMethods) {
                ExtRequestMapping methodExtRequestMapping = method.getDeclaredAnnotation(ExtRequestMapping.class);
                if (methodExtRequestMapping != null) {
                    String httpRequestUrl = methodExtRequestMapping.value();
                    mvcBeanUrl.put(requestBaseUrl + httpRequestUrl, mvcObject);
                    mvcMethodUrl.put(requestBaseUrl + httpRequestUrl, method.getName());
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //处理请求
            doDispatch(req, resp);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 1.获取请求url地址
        String requestUrl = req.getRequestURI();
        // 2.使用请求url查找对应mvc 控制器bean
        Object object = mvcBeanUrl.get(requestUrl);
        if (object == null) {
            resp.getWriter().println("http ext not found  controller 404");
            return;
        }
        // 3.获取对应的请求方法
        String methodName = mvcMethodUrl.get(requestUrl);
        if (StringUtils.isEmpty(methodName)) {
            resp.getWriter().println("http ext not found Method 404");
            return;
        }
        // 4.使用java反射技术执行方法
        Class<? extends Object> classInfo = object.getClass();
        String resultPage = (String) methodInvoke(classInfo, object, methodName);
        // 5.视图展示
        viewdisplay(resultPage, req, resp);
    }

    // 执行方法
    public Object methodInvoke(Class<? extends Object> classInfo, Object object, String methodName) {
        try {
            Method method = classInfo.getMethod(methodName);
            Object result = method.invoke(object);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 视图展示
    public void viewdisplay(String pageName, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // 获取后缀信息
        String suffix = ".jsp";
        // 页面目录地址
        String prefix = "/";
        req.getRequestDispatcher(prefix + pageName + suffix).forward(req, res);
    }

}
