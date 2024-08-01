package com.jd.boot001.mapper;

import com.jd.boot001.entity.AccountChangeEvent;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 操作银行2的账户
 * RocketMQ测试用例
 */
@Repository
//@Mapper
public interface Bank2AccountInfoMapper {

    /**
     * 账户余额修改
     *
     * @param accountNo
     * @param amount
     * @return
     */
    @Update("update account_info_bank2 set account_balance=account_balance+#{amount} where account_no=#{accountNo}")
    int updateAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

    /**
     * 查询事务id是否存在（用于RocketMQ事务回查）
     *
     * @param txNo
     * @return
     */
    @Select("select count(1) from de_duplication_bank2 where tx_no = #{txNo}")
    int isExistTx(String txNo);

    /**
     * 新增事务id（用于RocketMQ事务回查）
     *
     * @param accountChangeEvent
     * @return
     */
    @Insert("insert into de_duplication_bank2(tx_no, create_time, from_account, to_account, amount) " +
            "values (#{ev.txNo},now(),#{ev.fromAccountNo}, #{ev.toAccountNo}, #{ev.amount});")
    int addTx(@Param("ev") AccountChangeEvent accountChangeEvent);

    /**
     * 新增事务id（用于RocketMQ事务回查）
     * @param txNo
     * @return
     */
//    @Insert("insert into de_duplication_bank2(tx_no, create_time, from_account, to_account, amount) values(#{txNo},now(),#{fromAccount}, #{toAccount}, #{amount});")
//    int addTx(@Param("txNo") String txNo, @Param("fromAccount") String fromAccount, @Param("toAccount") String toAccount, @Param("amount") Double amount);

}
