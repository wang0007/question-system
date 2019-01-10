db.getCollection('activity.agenda').update({
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityAgendaDO",
    "withAmPmTitle": true,
    "agendas": [],
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('activity.agenda').update({
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityAgendaDO",
    "withAmPmTitle": true,
    "agendas": [],
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('activity.agenda').update({
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityAgendaDO",
    "withAmPmTitle": true,
    "agendas": [],
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});


db.getCollection('activity.barrage.setting').update({
    "conferenceId": "__default::zh_CN"
}, {
    "conferenceId": "__default::zh_CN",
    "_class": "com.neuq.question.data.pojo.ActivityBarrageSettingDO",
    "barrageSize": "LARGE",
    "barrageSpeed": 10,
    "clearBarrage": false,
    "enterFrequency": 500,
    "backgroundUrl": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
    "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg",
    "circulation": true,
    "fullScreen": true
}, {
    upsert: true
});

db.getCollection('activity.barrage.setting').update({
    "conferenceId": "__default::en_US"
}, {
    "conferenceId": "__default::en_US",
    "_class": "com.neuq.question.data.pojo.ActivityBarrageSettingDO",
    "barrageSize": "LARGE",
    "barrageSpeed": 10,
    "clearBarrage": false,
    "enterFrequency": 500,
    "backgroundUrl": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
    "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg",
    "circulation": true,
    "fullScreen": true
}, {
    upsert: true
});

// 更新弹幕设置脚本
db.getCollection('activity.barrage.setting').update({
        "_class": "com.neuq.question.data.pojo.ActivityBarrageSettingDO"
    },
    {
        $set: {
            "barrageSpeed": 10,
            "clearBarrage": false,
            "enterFrequency": 500
        }
    },
    false,
    true);

db.getCollection('activity.barrage.setting').update({
    "conferenceId": "__default::zh_TW"
}, {
    "conferenceId": "__default::zh_TW",
    "_class": "com.neuq.question.data.pojo.ActivityBarrageSettingDO",
    "barrageSize": "LARGE",
    "barrageSpeed": 10,
    "clearBarrage": false,
    "enterFrequency": 500,
    "backgroundUrl": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
    "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg",
    "circulation": true,
    "fullScreen": true
}, {
    upsert: true
});

db.getCollection('activity.feed.setting').update({
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN",
    "_class": "com.neuq.question.data.pojo.ActivityNewsFeedSettingDO",
    "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
    "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
}, {
    upsert: true
});

db.getCollection('activity.feed.setting').update({
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US",
    "_class": "com.neuq.question.data.pojo.ActivityNewsFeedSettingDO",
    "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
    "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
}, {
    upsert: true
});

db.getCollection('activity.feed.setting').update({
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW",
    "_class": "com.neuq.question.data.pojo.ActivityNewsFeedSettingDO",
    "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
    "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
}, {
    upsert: true
});


db.getCollection('activity.live.setting').update({
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityLiveSettingDO",
    "enable": false,
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('activity.live.setting').update({
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityLiveSettingDO",
    "enable": false,
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});


db.getCollection('activity.live.setting').update({
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityLiveSettingDO",
    "enable": false,
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});


db.getCollection('activity.lottery.setting').update({
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityLotterySettingDO",
    "projection": {
        "projectionType": "TURN_TABLE",
        "projectionBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
    },
    "scopeSetting": {
        "scope": "SIGN_IN"
    },
    "winnerMessageTemplate": "恭喜您，获得{{conference.name}}大会{{activity.name}}会场，{{prize.prize}}：{{prize.prizeName}}奖品一份，立即联系工作人员领取奖品吧",
    "blacklist": [],
    "prizes": [],
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('activity.lottery.setting').update({
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityLotterySettingDO",
    "projection": {
        "projectionType": "TURN_TABLE",
        "projectionBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
    },
    "scopeSetting": {
        "scope": "SIGN_IN"
    },
    "winnerMessageTemplate": "Congratulations. You won a prize in {{conference.name}} at {{activity.name}}: {{prize.prize}}: {{prize.prizeName}}. Please contact the staff immediately to claim the prize",
    "blacklist": [],
    "prizes": [],
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('activity.lottery.setting').update({
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_class": "com.neuq.question.data.pojo.ActivityLotterySettingDO",
    "projection": {
        "projectionType": "TURN_TABLE",
        "projectionBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
    },
    "scopeSetting": {
        "scope": "SIGN_IN"
    },
    "winnerMessageTemplate": "恭喜您，獲得{{conference.name}}大會{{activity.name}}會場，{{prize.prize}}：{{prize.prizeName}}獎品一份，立即聯繫工作人員領取獎品吧",
    "blacklist": [],
    "prizes": [],
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});


db.getCollection('activity.sign_in.setting').update({
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN",
    "_class": "com.neuq.question.data.pojo.ActivitySignInSettingDO",
    "enable": false,
    "scope": "SIGNUP_AND_GUEST",
    "projection": {
        "projectionType": "TWOD",
        "projectionBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
    },
    "joinGroupSetting": {
        "enable": false
    },
    "notifySetting": {
        "successMessageTemplate": "签到成功，您是第{{record.sequence}}位签到人员！",
        "failureMessageTemplate": "签到失败，请联系工作人员帮您签到！",
        "alreadySignInMessageTemplate": "签到成功，您是第{{record.sequence}}位签到人员！",
        "enable": false
    }
}, {
    upsert: true
});

db.getCollection('activity.sign_in.setting').update({
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US",
    "_class": "com.neuq.question.data.pojo.ActivitySignInSettingDO",
    "enable": false,
    "scope": "SIGNUP_AND_GUEST",
    "projection": {
        "projectionType": "TWOD",
        "projectionBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
    },
    "joinGroupSetting": {
        "enable": false
    },
    "notifySetting": {
        "successMessageTemplate": "Checked in successfully. You are the number {{record.sequence}}",
        "failureMessageTemplate": "Check-in failed. Please contact the staff to check in for you",
        "alreadySignInMessageTemplate": "Checked in successfully. You are the number {{record.sequence}}",
        "enable": false
    }
}, {
    upsert: true
});

db.getCollection('activity.sign_in.setting').update({
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW",
    "_class": "com.neuq.question.data.pojo.ActivitySignInSettingDO",
    "enable": false,
    "scope": "SIGNUP_AND_GUEST",
    "projection": {
        "projectionType": "TWOD",
        "projectionBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
    },
    "joinGroupSetting": {
        "enable": false
    },
    "notifySetting": {
        "successMessageTemplate": "簽到成功，您是第{{record.sequence}}位簽到人員！",
        "failureMessageTemplate": "簽到失敗，請聯繫工作人員幫您簽到！",
        "alreadySignInMessageTemplate": "簽到成功，您是第{{record.sequence}}位簽到人員！",
        "enable": false
    }
}, {
    upsert: true
});


db.getCollection('ai.face.conference.setting').update({
    "groupId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "groupId": "__default::zh_CN",
    "_class": "com.neuq.question.data.pojo.AiFaceConferenceSettingDO",
    "callbackUrl": "/rest/v1/client/{activityId}/signin/face",
    "failedRecognizeMessage": "签到失败，请联系工作人员帮您签到！",
    "registrationNotifySetting": {
        "enable": false
    },
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('ai.face.conference.setting').update({
    "groupId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    "groupId": "__default::en_US",
    "_class": "com.neuq.question.data.pojo.AiFaceConferenceSettingDO",
    "callbackUrl": "/rest/v1/client/{activityId}/signin/face",
    "failedRecognizeMessage": "Check-in failed. Please contact the staff to check in for you",
    "registrationNotifySetting": {
        "enable": false
    },
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('ai.face.conference.setting').update({
    "groupId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "groupId": "__default::zh_TW",
    "_class": "com.neuq.question.data.pojo.AiFaceConferenceSettingDO",
    "callbackUrl": "/rest/v1/client/{activityId}/signin/face",
    "failedRecognizeMessage": "簽到失敗，請聯繫工作人員幫您簽到！",
    "registrationNotifySetting": {
        "enable": false
    },
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});


db.getCollection('ai.face.activity.setting').update({
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "recognizeNotifySetting": {
        "enable": false,
        "succeedMessageTemplate": "签到成功，您是第{{record.sequence}}位签到人员！",
        "failedMessage": "签到失败，请联系工作人员帮您签到！",
        "duplicateMessageTemplate": "已经签到"
    },
    "_class": "com.neuq.question.data.pojo.AiFaceActivitySettingDO",
    "activityId": "__default::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});


db.getCollection('ai.face.activity.setting').update({
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    "recognizeNotifySetting": {
        "enable": false,
        "succeedMessageTemplate": "Checked in successfully. You are the number {{record.sequence}}",
        "failedMessage": "Check-in failed. Please contact the staff to check in for you",
        "duplicateMessageTemplate": "Checked in already"
    },
    "_class": "com.neuq.question.data.pojo.AiFaceActivitySettingDO",
    "activityId": "__default::en_US",
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('ai.face.activity.setting').update({
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "recognizeNotifySetting": {
        "enable": false,
        "succeedMessageTemplate": "簽到成功，您是第{{record.sequence}}位簽到人員！",
        "failedMessage": "簽到失敗，請聯繫工作人員幫您簽到！",
        "duplicateMessageTemplate": "已經簽到"
    },
    "_class": "com.neuq.question.data.pojo.AiFaceActivitySettingDO",
    "activityId": "__default::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});


db.getCollection('conference.background').update({
    "_id": "conference::background"
}, {
    "_id": "conference::background",
    "backgroundType": "COMMON",
    "background": [{
        "backgroundId": "_defaultid1",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/f2f14520-6182-471b-acfd-2f0377c7387c.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid2",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/5c88cf7a-f524-4667-9cd3-2a1b14fc3c4c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/26/5c88cf7a-f524-4667-9cd3-2a1b14fc3c4c.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid3",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/27/9b838b34-59f8-4a05-810c-37911befa070.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/27/9b838b34-59f8-4a05-810c-37911befa070.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid4",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/28/1da1a0ac-5aa0-43d4-abde-c6f000e04c30.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/28/1da1a0ac-5aa0-43d4-abde-c6f000e04c30.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid5",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/28/3e278f2d-eaff-4253-b049-51179359e195.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/28/3e278f2d-eaff-4253-b049-51179359e195.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid6",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/29/8a93364e-9d5b-416a-9461-a41b469e7aa6.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/29/8a93364e-9d5b-416a-9461-a41b469e7aa6.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid7",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/29/ed6943c3-113f-4c7f-b54a-cea7000f658b.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/29/ed6943c3-113f-4c7f-b54a-cea7000f658b.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid8",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/30/792a1b00-a16a-4e3e-92a3-15a6f65799be.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/30/792a1b00-a16a-4e3e-92a3-15a6f65799be.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid9",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/31/a73a3b3a-f8b6-407e-8bd4-7b4a7f322e63.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/15/11/31/a73a3b3a-f8b6-407e-8bd4-7b4a7f322e63.jpg.thumb.jpg"
    }]
}, {
    upsert: true
});


db.getCollection('conference.background').update({
    "_id": "conference::signup::background"
}, {
    "_id": "conference::signup::background",
    "backgroundType": "COMMON",
    "background": [{
        "backgroundId": "_defaultid1",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/39/4ce9604e-2ebb-4788-8611-80bad2473372.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/39/4ce9604e-2ebb-4788-8611-80bad2473372.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid2",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/39/a09f6dca-985b-43ee-a6d0-3b1ea7453f6d.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/39/a09f6dca-985b-43ee-a6d0-3b1ea7453f6d.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid3",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/40/5191f9dd-e755-49d1-b202-3c47ebd709a6.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/40/5191f9dd-e755-49d1-b202-3c47ebd709a6.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid4",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/41/7f67a4f3-5671-44d9-9312-e01049c6b7b8.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/41/7f67a4f3-5671-44d9-9312-e01049c6b7b8.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid5",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/41/5ed12700-95d2-44b9-8391-21025cd4afbf.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/41/5ed12700-95d2-44b9-8391-21025cd4afbf.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid6",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/42/b3827121-71e7-4abc-9f5d-9a3cb074001c.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/42/b3827121-71e7-4abc-9f5d-9a3cb074001c.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid7",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/42/ca1939e2-f89b-4506-b452-2aff3e5dafdb.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/42/ca1939e2-f89b-4506-b452-2aff3e5dafdb.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid8",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/43/c077073c-bd8b-4c07-ab46-02e5b9a8a158.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/43/c077073c-bd8b-4c07-ab46-02e5b9a8a158.jpg.thumb.jpg"
    }, {
        "backgroundId": "_defaultid9",
        "background": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/44/8a40d581-963f-4c6e-998a-e1afc77f8dff.jpg",
        "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/44/8a40d581-963f-4c6e-998a-e1afc77f8dff.jpg.thumb.jpg"
    }]
}, {
    upsert: true
});


db.getCollection('conference.guide').update({
    "_id": "0_plane::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_id": "0_plane::zh_CN",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/09/a23b08c8-27f0-47da-a4c0-c88c3b6c891c.png",
    "name": "接送机服务",
    "items": [],
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "1_meetingPlace::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_id": "1_meetingPlace::zh_CN",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/14/48/e73149bf-c2e3-4330-a4bc-23e47b97d051.png",
    "name": "会议地点",
    "items": [],
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "2_dinner::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_id": "2_dinner::zh_CN",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/21/cfbeea6f-700d-4f27-8725-731bd430e80a.png",
    "name": "用餐安排",
    "items": [],
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "3_stay::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_id": "3_stay::zh_CN",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/10/0d23728f-4773-4a0e-b4e0-e685f587827a.png",
    "name": "住宿推荐",
    "items": [],
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "9_customize::zh_CN",
    "conferenceId": "__default::zh_CN"
}, {
    "_id": "9_customize::zh_CN",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/10/ae7b3065-a6f4-4621-92d4-dca136ecbc82.png",
    "name": "自定义",
    "items": [],
    "conferenceId": "__default::zh_CN"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "0_plane::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_id": "0_plane::en_US",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/09/a23b08c8-27f0-47da-a4c0-c88c3b6c891c.png",
    "name": "Arrive",
    "items": [],
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "1_meetingPlace::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_id": "1_meetingPlace::en_US",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/14/48/e73149bf-c2e3-4330-a4bc-23e47b97d051.png",
    "name": "Location",
    "items": [],
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "2_dinner::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_id": "2_dinner::en_US",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/21/cfbeea6f-700d-4f27-8725-731bd430e80a.png",
    "name": "Meals",
    "items": [],
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "3_stay::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_id": "3_stay::en_US",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/10/0d23728f-4773-4a0e-b4e0-e685f587827a.png",
    "name": "Accommodation",
    "items": [],
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "9_customize::en_US",
    "conferenceId": "__default::en_US"
}, {
    "_id": "9_customize::en_US",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/10/ae7b3065-a6f4-4621-92d4-dca136ecbc82.png",
    "name": "Self-defined",
    "items": [],
    "conferenceId": "__default::en_US"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "0_plane::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_id": "0_plane::zh_TW",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/09/a23b08c8-27f0-47da-a4c0-c88c3b6c891c.png",
    "name": "接送機服務",
    "items": [],
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "1_meetingPlace::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_id": "1_meetingPlace::zh_TW",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/14/48/e73149bf-c2e3-4330-a4bc-23e47b97d051.png",
    "name": "會議地點",
    "items": [],
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "2_dinner::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_id": "2_dinner::zh_TW",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/21/cfbeea6f-700d-4f27-8725-731bd430e80a.png",
    "name": "用餐安排",
    "items": [],
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "3_stay::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_id": "3_stay::zh_TW",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/10/0d23728f-4773-4a0e-b4e0-e685f587827a.png",
    "name": "住宿推薦",
    "items": [],
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});

db.getCollection('conference.guide').update({
    "_id": "9_customize::zh_TW",
    "conferenceId": "__default::zh_TW"
}, {
    "_id": "9_customize::zh_TW",
    "_class": "com.neuq.question.data.pojo.ConferenceGuideDO",
    "icon": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/07/31/15/10/ae7b3065-a6f4-4621-92d4-dca136ecbc82.png",
    "name": "自訂",
    "items": [],
    "conferenceId": "__default::zh_TW"
}, {
    upsert: true
});


db.getCollection('activity.sign_up.form').update({
    "conferenceId": "__default"
}, {
    "conferenceId": "__default",
    "backgroundImage": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/39/4ce9604e-2ebb-4788-8611-80bad2473372.jpg",
    "thumbBackground": "https://ykj-yyim-test.oss-cn-beijing.aliyuncs.com/conference/2018/08/23/14/39/4ce9604e-2ebb-4788-8611-80bad2473372.jpg.thumb.jpg",
    "fields": [{
        "formFieldId": "__name",
        "label": "姓名",
        "i18nLabel": {
            "zh_CN": "姓名",
            "zh_TW": "姓名",
            "en_US": "Name"
        },
        "required": true,
        "selected": true,
        "allowModify": false,
        "order": 1
    }, {
        "formFieldId": "__mobile",
        "label": "手机号",
        "i18nLabel": {
            "zh_CN": "手机号",
            "zh_TW": "手機號",
            "en_US": "Mobile"
        },
        "required": true,
        "selected": true,
        "allowModify": false,
        "order": 2
    }, {
        "formFieldId": "__identify_code",
        "label": "验证码",
        "i18nLabel": {
            "zh_CN": "验证码",
            "zh_TW": "驗證碼",
            "en_US": "Verification Code"
        },
        "required": true,
        "selected": true,
        "allowModify": false,
        "order": 3
    }, {
        "formFieldId": "__invite_code",
        "label": "邀请码",
        "i18nLabel": {
            "zh_CN": "邀请码",
            "zh_TW": "邀請碼",
            "en_US": "Invitation Code"
        },
        "required": true,
        "selected": true,
        "allowModify": false,
        "order": 4
    }]
}, {
    upsert: true
});


db.getCollection('conference.app').update({
    "_id": "__default",
    "conferenceId": "__default"
}, {
    "_id": "__default",
    "_class": "com.neuq.question.data.pojo.ConferenceAppDO",
    "conferenceId": "__default",
    "apps": [{
        "appId": "_guide",
        "icon": "http://140.143.133.139/files/app/app_guide.png",
        "i18nName": {
            "zh_CN": "大会指南",
            "zh_TW": "大會指南",
            "en_US": "Guideline"
        },
        "name": "大会指南",
        "enable": false,
        "url": "#/conferenceGuide?conferenceId=${conferenceId}"

    }, {
        "appId": "_info",
        "icon": "http://140.143.133.139/files/app/app_info.png",
        "i18nName": {
            "zh_CN": "我的信息",
            "zh_TW": "我的信息",
            "en_US": "My Info"
        },
        "name": "我的信息",
        "enable": false,
        "url": "#/conferenceMy?conferenceId=${conferenceId}"
    },  {
        "appId": "_lottery",
        "icon": "http://140.143.133.139/files/app/app_lottery.png",
        "i18nName": {
            "zh_CN": "大会奖品",
            "zh_TW": "大會獎品",
            "en_US": "Lucky Draw"
        },
        "name": "大会奖品",
        "enable": false,
        "url": "#/conferencePrize?conferenceId=${conferenceId}"
    }, {
        "appId": "_signin",
        "icon": "http://140.143.133.139/files/app/app_signin.png",
        "i18nName": {
            "zh_CN": "大会签到",
            "zh_TW": "大會簽到",
            "en_US": "Check In"
        },
        "name": "大会签到",
        "enable": false,
        "url": "#/conferenceSign"
    }, {
        "appId": "_agenda",
        "icon": "http://140.143.133.139/files/app/app_agenda.png",
        "i18nName": {
            "zh_CN": "大会议程",
            "zh_TW": "大會議程",
            "en_US": "Agenda"
        },
        "name": "大会议程",
        "enable": false,
        "url": "#/conferenceAgenda?conferenceId=${conferenceId}"
    }, {
        "appId": "_signin_proxy",
        "icon": "http://140.143.133.139/files/app//app_signin_proxy.png",
        "i18nName": {
            "zh_CN": "帮他签到",
            "zh_TW": "幫他簽到",
            "en_US": "Help Others"
        },
        "name": "帮他签到",
        "enable": false,
        "url": "#/conferenceHelpSign"
    }, {
        "appId": "_collect_face",
        "icon": "http://140.143.133.139/files/app/app_collect_face.png",
        "i18nName": {
            "zh_CN": "答题闯关",
            "zh_TW": "答题闯关",
            "en_US": "question"
        },
        "name": "集颜值",
        "enable": false,
        "url": "http://172.20.1.177:6058/conference/app/assets/activity/scan.html?conferenceId=${conferenceId}"
    }, {
        "appId": "_live",
        "icon": "http://140.143.133.139/files/app/app_live.png",
        "i18nName": {
            "zh_CN": "排行榜",
            "zh_TW": "排行榜",
            "en_US": "Live"
        },
        "name": "排行榜",
        "enable": false,
        "url": "#/conferenceVideo?conferenceId=${conferenceId}"
    },{
        "appId": "_barrage",
        "icon": "http://140.143.133.139/files/app//app_barrage.png",
        "i18nName": {
            "zh_CN": "发弹幕",
            "zh_TW": "发彈幕",
            "en_US": "Barrage"
        },
        "name": "发弹幕",
        "enable": false,
        "url": "#/conferenceBarrage?conferenceId=${conferenceId}"
    }]

}, {
    upsert: true
});
