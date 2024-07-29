package com.jd.boot001.mapper;

import org.apache.ibatis.annotations.*;

/**
 * 用户账户操作
 * RocketMQ测试用例
 */
@Mapper
public interface AccountInfoDao {

    /**
     * 账户余额修改
     * @param accountNo
     * @param amount
     * @return
     */
    @Update("update account_info set account_balance=account_balance+#{amount} where account_no=#{accountNo}")
    int updateAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

    /**
     * 查询事务id是否存在（用于RocketMQ事务回查）
     * @param txNo
     * @return
     */
    @Select("select count(1) from de_duplication where tx_no = #{txNo}")
    int isExistTx(String txNo);

    /**
     * 新增事务id（用于RocketMQ事务回查）
     * @param txNo
     * @return
     */
    @Insert("insert into de_duplication values(#{txNo},now());")
    int addTx(String txNo);

}
