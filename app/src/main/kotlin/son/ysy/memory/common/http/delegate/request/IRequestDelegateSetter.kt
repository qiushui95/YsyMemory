package son.ysy.memory.common.http.delegate.request

interface IRequestDelegateSetter<T: Any> : IRequestDelegateBusySetter, IRequestDelegateErrorSetter,
    IRequestDelegateDataSetter<T>, IRequestDelegateStatusSetter<T>