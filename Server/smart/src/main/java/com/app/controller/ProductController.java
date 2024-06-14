package com.app.controller;

import com.app.controller.ex.*;
import com.app.service.ProductService;
import com.app.service.UserService;
import com.app.util.CusAccessObjectUtil;
import com.app.util.JsonResult;
import com.app.entity.*;
import com.app.util.JwtUtil;
import com.app.vo.Detail;
import com.app.vo.Display;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.cglib.core.TinyBitSet;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api("商品管理")
@RequestMapping("product")
public class ProductController extends BaseController{
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    /** 商品图片文件大小的上限值(10MB) */
    public static final int PHOTO_MAX_SIZE = 10 * 1024 * 1024;
    /** 允许上传的商品图片的文件类型 */
    public static final List<String> PHOTO_TYPES = new ArrayList<String>();

    /** 初始化允许上传的图片的文件类型 */
    static {
        PHOTO_TYPES.add("image/jpeg");
        PHOTO_TYPES.add("image/png");
        PHOTO_TYPES.add("image/bmp");
        PHOTO_TYPES.add("image/gif");
    }


    @PostMapping("addSellpro")
    public JsonResult<Void> addSellProduct(String text, Integer num, String introdu, String introduce, double price, @RequestPart MultipartFile file, HttpServletRequest request){

        Product prod = new Product();

        prod.setText(text);
        prod.setNum(num);
        prod.setIntrodu(introdu);
        prod.setIntroduce(introduce);

        if(file.isEmpty()){
            throw new FileEmptyException("文件为空");
        }
        if(file.getSize() > PHOTO_MAX_SIZE){
            throw new FileSizeException("文件超出限制");
        }
        String contentType = file.getContentType();
        if (!PHOTO_TYPES.contains(contentType)) {
            throw new FileTypeException("文件类型不支持");
        }

        //String parent = "/var/server/public/storage/uploaded/ProImage";
        String parent = "/var/server/storage/app/uploaded/ProImage";
//        ApplicationHome ah = new ApplicationHome(getClass());
//        File file3 = ah.getSource();
//        String parent = file3.getParentFile()+"/proImage".toString();

        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString() + suffix;

        File dest = new File(dir, filename);
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            // 抛出异常
            throw new FileStateException("文件状态异常，可能文件已被移动或删除");
        } catch (IOException e) {
            // 抛出异常
            throw new FileUploadIOException("上传文件时读写错误，请稍后重新尝试");
        }
        String image = "/var/server/public/storage/uploaded/ProImage/" + filename;
        prod.setImage(image);

        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring("Bearer ".length());
        String sign = "qiyaoyyds";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sign)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        String phone = verify.getClaim("phone").asString();

        // User user = userService.findIp(phone);
        //String ip = request.getHeader("x-forwarded-for");
        String ip = request.getHeader("X-Real-IP");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }

        String url = "https://whois.pconline.com.cn/ipJson.jsp?ip=" + ip + "&json=true";
        String str = productService.getAddr(url);
        try{
            Ipaddr ipaddr = new ObjectMapper().readValue(str, Ipaddr.class);
            String address;
            if(ipaddr.getPro().equals(ipaddr.getCity())){
                address = ipaddr.getCity();
            }else{
                address = ipaddr.getPro() + ipaddr.getCity();
            }

            prod.setIpaddr(address);
        }catch (IOException e) {
            e.printStackTrace();
        }

        prod.setPrice(price);

        Integer status = 1;
        Date time = new Date();
        Integer type = 1;
        prod.setType(type);
        prod.setTime(time);
        prod.setStatus(status);
        prod.setPhone(phone);

        productService.proInsert(prod);

        return new JsonResult<Void>(OK);
    }

    @PostMapping("addExpro")
    public JsonResult<Void> addExProduct(String text, Integer num, String introdu, String introduce, @RequestPart MultipartFile file, HttpServletRequest request){

        Product prod = new Product();

        prod.setText(text);
        prod.setNum(num);
        prod.setIntrodu(introdu);
        prod.setIntroduce(introduce);

        if(file.isEmpty()){
            throw new FileEmptyException("文件为空");
        }
        if(file.getSize() > PHOTO_MAX_SIZE){
            throw new FileSizeException("文件超出限制");
        }
        String contentType = file.getContentType();
        if (!PHOTO_TYPES.contains(contentType)) {
            throw new FileTypeException("文件类型不支持");
        }

        String parent = "/var/server/storage/app/uploaded/ProImage";
        //String parent = "/var/server/public/storage/uploaded/ProImage";
//        ApplicationHome ah = new ApplicationHome(getClass());
//        File file3 = ah.getSource();
//        String parent = file3.getParentFile()+"/proImage".toString();


        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString() + suffix;

        File dest = new File(dir, filename);
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            // 抛出异常
            throw new FileStateException("文件状态异常，可能文件已被移动或删除");
        } catch (IOException e) {
            // 抛出异常
            throw new FileUploadIOException("上传文件时读写错误，请稍后重新尝试");
        }
        String image = "/var/server/public/storage/uploaded/ProImage/" + filename;
        prod.setImage(image);

        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring("Bearer ".length());
        String sign = "qiyaoyyds";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sign)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        String phone = verify.getClaim("phone").asString();


        // User user = userService.findIp(phone);
        // String ip = user.getLast_login_ip();
//        String ip = request.getHeader("x-forwarded-for");
        String ip = request.getHeader("X-Real-IP");
        String url = "https://whois.pconline.com.cn/ipJson.jsp?ip=" + ip + "&json=true";
        String str = productService.getAddr(url);
        try{
            Ipaddr ipaddr = new ObjectMapper().readValue(str, Ipaddr.class);
            String address;
            if(ipaddr.getPro().equals(ipaddr.getCity())){
                address = ipaddr.getCity();
            }else{
                address = ipaddr.getPro() + ipaddr.getCity();
            }
            prod.setIpaddr(address);
        }catch (IOException e) {
            e.printStackTrace();
        }

        double price = 0.0;
        prod.setPrice(price);

        Integer status = 1;
        Date time = new Date();
        Integer type = 2;
        prod.setType(type);
        prod.setTime(time);
        prod.setStatus(status);
        prod.setPhone(phone);

        productService.proInsert(prod);

        return new JsonResult<Void>(OK);
    }

//    {
//        "text":"好好好",
//        "price":250,
//        "num":25,
//        "introdu":"tql",
//        "introduce":"fjdiaojgaijgpqg"
//    }

//    D:\2.5\Javacode\APP\app\target/proImage/4060d5be-d914-43f7-a462-a4731e3e2c52.jpg


    private InputStream getImgInputStream(String imgPath) throws FileNotFoundException {
        return new FileInputStream(new File(imgPath));
    }

    @GetMapping("proImage")
    public void proImage(HttpServletResponse resp, Integer pid) throws IOException {

        Product pro = productService.findImage(pid);
        String imgPath = pro.getImage();
        String str = imgPath.substring(imgPath.length()-4);

                // "D:\\2.png";
        final InputStream in = getImgInputStream(imgPath);
        if(str.equals(".jpg") || str.equals(".jepg")){
            resp.setContentType(MediaType.IMAGE_JPEG_VALUE);
        }else if(str.equals(".png")){
            resp.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        IOUtils.copy(in, resp.getOutputStream());
    }

//    @GetMapping("headImage")
//    public void headImage(HttpServletResponse resp, Integer pid) throws IOException {
//
//        String phone = productService.findPhone(pid).getPhone();
//        User user = userService.findHead(phone);
//        //String imgPath = user.getHeadImgPath();
//        String str = imgPath.substring(imgPath.length()-4);
//
//        // "D:\\2.png";
//        final InputStream in = getImgInputStream(imgPath);
//        if(str.equals(".jpg") || str.equals(".jepg")){
//            resp.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        }else if(str.equals(".png")){
//            resp.setContentType(MediaType.IMAGE_PNG_VALUE);
//        }
//        IOUtils.copy(in, resp.getOutputStream());
//    }


    @GetMapping("search")
    public JsonResult<List<Display>> findSell(int type){
        List<Display> data = productService.findByType(type);
        return new JsonResult<List<Display>>(OK, data);
    }

    @GetMapping("show")
    public JsonResult<Detail> show(Integer pid){
        Detail pro = productService.show(pid);
        productService.clickPro(pid);
        return new JsonResult<Detail>(OK, pro);
    }

    @RequestMapping("delete")
    public JsonResult<Void> proDelete(Integer pid){
        productService.proDelete(pid);
        return new JsonResult<Void>(OK);
    }

    @RequestMapping("offshelf")
    public JsonResult<Void> off(Integer pid){
        productService.proOff(pid);
        return new JsonResult<Void>(OK);
    }


    @RequestMapping("givelike")
    public JsonResult<Void> giveLike(Integer pid, HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring("Bearer ".length());

        String sign = "qiyaoyyds";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sign)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        String phone = verify.getClaim("phone").asString();

        productService.giveLike(pid, phone);
        return new JsonResult<Void>(OK);
    }

    @GetMapping("clickChart")
    public JsonResult<List<Display>> clickChart(){
        List<Display> list = productService.clickChart();
        return new JsonResult<List<Display>>(OK, list);
    }

    @GetMapping("likeChart")
    public JsonResult<List<Display>> likeChart(){
        List<Display> list = productService.likeChart();
        return new JsonResult<List<Display>>(OK, list);
    }

    @GetMapping("heatChart")
    public JsonResult<List<Display>> heatChart(){
        List<Display> list = productService.heatChart();
        return new JsonResult<List<Display>>(OK, list);
    }

    @GetMapping("creat")
    public Phone creat(Phone phone){
        //String p = "136136136";
        phone.setToken(JwtUtil.createToken());
        return phone;
    }

    @GetMapping("userPro")
    public JsonResult<List<Display>> userPro(String phone){
        List<Display> list = productService.userPro(phone);
        return new JsonResult<List<Display>>(OK, list);
    }

//    @GetMapping("login")
//    public TokenUser login(TokenUser tuser){
//        String username = "tom";
//        String password = "123456";
//
//        if(username.equals(tuser.getUsername()) && password.equals(tuser.getPassword())){
//            // 创建token:token保存到tuser对象中
//            tuser.setToken(JwtUtil.createToken());
//            return tuser;
//        }
//        return null;
//    }

//    @GetMapping("check_token")
//    public void checkToken(HttpServletRequest request){
//        String token = request.getHeader("Authorization");
//        String sign = "qiyaoyyds";
//        JwtParser jwtParser = Jwts.parser();
//        Jws<Claims> claimsJws = jwtParser.setSigningKey(sign).parseClaimsJws(token);
//        Claims claims = claimsJws.getBody();
//        System.out.println(claims.get("phone"));
//        //Claims claims = JwtUtil.parseJwt(token);
//
//    }


//    @GetMapping("check_token")
//    public void checkToken(HttpServletRequest request) {
//        String token = request.getHeader("token");
//        if (JwtUtil.checkToken(token) && JwtUtil.checkJwt(token)) {
//            String sign = "qiyaoyyds";
//            JwtParser jwtParser = Jwts.parser();
//            Jws<Claims> claimsJws = jwtParser.setSigningKey(sign).parseClaimsJws(token);
//            Claims claims = claimsJws.getBody();
//            System.out.println(claims.get("username"));
//            System.out.println(claims.get("role"));
//            //Claims claims = JwtUtil.parseJwt(token);
//        } else {
//            System.out.println("false");
//        }
//    }

//    @GetMapping("doPost")
//    public static void doPost(JSONObject date) {
//        HttpClient client = HttpClients.createDefault();
//        // 要调用的接口方法
//        String url = "https://whois.pconline.com.cn/ipJson.jsp?ip=112.54.168.105&json=true";
//        HttpPost post = new HttpPost(url);
//        JSONObject jsonObject = null;
//        try {
//            StringEntity s = new StringEntity(date.toString());
//            s.setContentEncoding("UTF-8");
//            s.setContentType("application/json");
//            post.setEntity(s);
//            post.addHeader("content-type", "text/xml");
//            HttpResponse res = client.execute(post);
//            String response1 = EntityUtils.toString(res.getEntity());
//            System.out.println(response1);
//            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
//                System.out.println(result);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


//    @RequestMapping("getip")
//    public void hello(){
//
//        String url = "https://whois.pconline.com.cn/ipJson.jsp?ip=112.54.168.105&json=true";
//        String str = productService.getAddr(url);
//        try{
//            Ipaddr ip = new ObjectMapper().readValue(str, Ipaddr.class);
//            System.out.println(ip.getPro());
//            System.out.println(ip.getCity());
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
