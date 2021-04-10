package com.strix.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.strix.yygh.cmn.listener.DictListener;
import com.strix.yygh.cmn.mapper.DictMapper;
import com.strix.yygh.cmn.service.DictService;
import com.strix.yygh.model.cmn.Dict;
import com.strix.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

//    根据数据id查询子数据列表
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        for(Dict dict:dictList){
            Long dickId = dict.getId();
            dict.setHasChildren(this.isChildren(dickId));
        }
        return dictList;
    }
//      导出数据接口
@Override
    public void exportDictData(HttpServletResponse response) {
    //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        //查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        //Dict -- DictEeVo
        List<DictEeVo> dictVoList = new ArrayList<>();
    for(Dict dict:dictList) {
        DictEeVo dictEeVo = new DictEeVo();
        // dictEeVo.setId(dict.getId());
        BeanUtils.copyProperties(dict,dictEeVo);
        dictVoList.add(dictEeVo);
    }
//      调用方法进行写操作
    try {
        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                .doWrite(dictVoList);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

//    导入数据字典
    @Override
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper))
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    判断id下面是否有子节点
    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
