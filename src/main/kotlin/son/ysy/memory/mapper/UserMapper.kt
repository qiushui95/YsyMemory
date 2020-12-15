package son.ysy.memory.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {

    /**
     *通过手机号+密码验证用户
     * @param phone 手机号
     * @param password 密码
     * @return true 验证通过
     */
    @Select("select count(*)>0 from t_user where c_phone=#{phone} and c_password=#{password};")
    fun checkUserByPhoneAndPassword(phone: String, password: String): Boolean

    /**
     *通过手机号判断用户是否已注册
     * @param phone 手机号
     * @return true 已注册
     */
    @Select("select count(*)>0 from t_user where c_phone=#{phone};")
    fun checkUserRegistered(phone: String): Boolean

    /**
     * 通过手机号返回称呼
     * @param phone 手机号
     * @return 称呼
     */
    @Select("select c_marker from t_user where c_phone =#{phone} limit 1;")
    fun getMarkerByPhone(phone: String): String?
}