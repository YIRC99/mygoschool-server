package yirc.mygoschool.Dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.domain.Userinfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class CarshareorderDto extends Carshareorder {
    private Userinfo createUserInfo;
    private Userinfo receiveUserInfo;
}