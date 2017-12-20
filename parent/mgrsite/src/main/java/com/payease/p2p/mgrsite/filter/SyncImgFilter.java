package com.payease.p2p.mgrsite.filter;


import com.payease.p2p.base.util.BidConst;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 同步图片拦截器
 * Created by liuxiaoming on 2017/8/17.
 */
public class SyncImgFilter implements Filter {

    private ServletContext ctx;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.ctx = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        File sourceFile = new File(ctx.getRealPath(req.getRequestURI()));
        System.out.println("图片同步:"+ctx.getRealPath(req.getRequestURI()));

        if(sourceFile.exists()){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            File publicFile = new File(BidConst.PUBLIC_IMG_SHARE_PATH, FilenameUtils.getName(req.getRequestURI()));
            if(publicFile.exists()){
                FileUtils.copyFile(publicFile,sourceFile);
                // 再放行;
                resp.setHeader("Cache-Control", "no-store");
                resp.setHeader("Pragma", "no-cache");
                resp.setDateHeader("Expires", 0);
                ServletOutputStream responseOutputStream = servletResponse
                        .getOutputStream();
                responseOutputStream.write(FileUtils
                        .readFileToByteArray(publicFile));
                responseOutputStream.flush();
                responseOutputStream.close();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
