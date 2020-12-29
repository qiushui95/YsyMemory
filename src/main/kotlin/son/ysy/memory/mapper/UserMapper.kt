package son.ysy.memory.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {

    /**
     *通过手机号+密码获取用户id
     * @param phone 手机号
     * @param password 密码
     * @return id
     */
    @Select("select c_id from t_user where c_phone=#{phone} and c_password=#{password} limit 1;")
    fun getIdByPhoneAndPassword(phone: String, password: String): String?

    /**
     *通过手机号判断用户是否已注册
     * @param phone 手机号
     * @return true 已注册
     */
    @Select("select count(*)>0 from t_user where c_phone=#{phone};")
    fun checkUserRegistered(phone: String): Boolean

    /**
     * 通过手机号返回称呼
     * @param id 用户id
     * @return 称呼
     */
    @Select("select c_marker from t_user where c_id =#{id}::uuid limit 1;")
    fun getMarkerById(id: String): String?
}