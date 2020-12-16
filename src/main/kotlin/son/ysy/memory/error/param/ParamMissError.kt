package son.ysy.memory.error.param

import son.ysy.memory.error.ApiError

abstract class ParamMissError(
    paramType: String,
    paramName: String
) : ApiError(msg = "${paramType}参数{${paramName}}缺失,请检查", data = null)