package com.jd.boot001.mapper;

import com.jd.boot001.entity.AccountChangeEvent;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 用户账户操作
 * RocketMQ测试用例
 */
// 入口类已经加注解@MapperScan，不需要下面2个注解
@Repository // spring注解，生成bean，被spring管理，被引用的地方不会标红
// @Mapper // mybatis注解，被引用的地方会标红，
// 三者区别见：https://blog.csdn.net/weixin_45188218/article/details/138085639
public interface Bank1AccountInfoMapper {

    /**
     * 账户余额修改
     *
     * @param accountNo
     * @param amount
     * @return
     */
    @Update("update account_info_bank1 set account_balance=account_balance+#{amount} where account_no=#{accountNo}")
    int updateAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

    /**
     * 查询事务id是否存在（用于RocketMQ事务回查）
     *
     * @param txNo
     * @return
     */
    @Select("select count(1) from de_duplication_bank1 where tx_no = #{txNo}")
    int isExistTx(String txNo);

    /**
     * 新增事务id（用于RocketMQ事务回查）
     *
     * @param accountChangeEvent
     * @return
     */
    @Insert("insert into de_duplication_bank1(tx_no, create_time, from_account, to_account, amount) " +
            "values (#{ev.txNo},now(),#{ev.fromAccountNo}, #{ev.toAccountNo}, #{ev.amount});")
    int addTx(@Param("ev") AccountChangeEvent accountChangeEvent);

    /**
     * 新增事务id（用于RocketMQ事务回查）
     * @param txNo
     * @return
     */
//    @Insert("insert into de_duplication_bank1(tx_no, create_time, from_account, to_account, amount) values (#{txNo},now(),#{fromAccount}, #{toAccount}, #{amount});")
//    int addTx(@Param("txNo") String txNo, @Param("fromAccount") String fromAccount, @Param("toAccount") String toAccount, @Param("amount") Double amount);

}
