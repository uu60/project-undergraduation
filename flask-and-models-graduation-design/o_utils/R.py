class R:
    codeKey = "code"
    msgKey = "msg"

    def __init__(self):
        super().__init__()
        self.data = {}
        self.update({self.__class__.codeKey: 200, self.__class__.msgKey: "success"}, )

    def update(self, dictionary):
        self.data.update(dictionary)

    @classmethod
    def error(cls, code, msg=None):
        r = cls()
        r.update({cls.codeKey: code}, )
        if msg is not None:
            r.update({cls.msgKey: msg}, )
        return r

    @classmethod
    def ok(cls, msg=None):
        r = cls()
        if msg is not None:
            r.update({cls.msgKey: msg}, )
        return r

    def put(self, key, value):
        self.update({key, value})
        return self

    def get(self):
        return self.data
