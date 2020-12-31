package son.ysy.memory.common.http.delegate.request

interface IRequestDelegateGetter<T: Any> : IRequestDelegateBusyGetter, IRequestDelegateErrorGetter,
    IRequestDelegateDataGetter<T>, IRequestDelegateStatusGetter<T>