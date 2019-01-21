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
    "barrageSpeed": "FAST",
    "backgroundUrl": "123.206.41.246/assest/barrage/setting.jpg",
    "thumbBackground": "123.206.41.246/assest/barrage/setting_thumb.jpg",
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
    "barrageSpeed": "FAST",
    "backgroundUrl": "123.206.41.246/assest/barrage/setting.jpg",
    "thumbBackground": "123.206.41.246/assest/barrage/setting_thumb.jpg",
    "circulation": true,
    "fullScreen": true
}, {
    upsert: true
});

db.getCollection('activity.barrage.setting').update({
    "conferenceId": "__default::zh_TW"
}, {
    "conferenceId": "__default::zh_TW",
    "_class": "com.neuq.question.data.pojo.ActivityBarrageSettingDO",
    "barrageSize": "LARGE",
    "barrageSpeed": "FAST",
    "backgroundUrl": "123.206.41.246/assest/barrage/setting.jpg",
    "thumbBackground": "123.206.41.246/assest/barrage/setting_thumb.jpg",
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
    "background": "123.206.41.246/assest/feed/setting.jpg",
    "thumbBackground": "123.206.41.246/assest/feed/setting_thumb.jpg"
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
    "background": "123.206.41.246/assest/feed/setting.jpg",
    "thumbBackground": "123.206.41.246/assest/feed/setting_thumb.jpg"
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
    "background": "123.206.41.246/assest/feed/setting.jpg",
    "thumbBackground": "123.206.41.246/assest/feed/setting_thumb.jpg"
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
        "projectionBackground": "123.206.41.246/assest/lottery/setting.jpg",
        "thumbBackground": "123.206.41.246/assest/lottery/setting_thumb.jpg"
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
        "projectionBackground": "123.206.41.246/assest/lottery/setting.jpg",
        "thumbBackground": "123.206.41.246/assest/lottery/setting_thumb.jpg"
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
        "projectionBackground": "123.206.41.246/assest/lottery/setting.jpg",
        "thumbBackground": "123.206.41.246/assest/lottery/setting_thumb.jpg"
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
        "projectionBackground": "123.206.41.246/assest/sign_in/setting.jpg",
        "thumbBackground": "123.206.41.246/assest/sign_in/setting_thumb.jpg"
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
        "projectionBackground": "123.206.41.246/assest/sign_in/setting.jpg",
        "thumbBackground": "123.206.41.246/assest/sign_in/setting_thumb.jpg"
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
        "projectionBackground": "123.206.41.246/assest/sign_in/setting.jpg",
        "thumbBackground": "123.206.41.246/assest/sign_in/setting_thumb.jpg"
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
        "background": "123.206.41.246/assest/background/defaultid1.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid1_thumb.jpg"
    }, {
        "backgroundId": "_defaultid2",
        "background": "123.206.41.246/assest/background/defaultid2.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid2_thumb.jpg"
    }, {
        "backgroundId": "_defaultid3",
        "background": "123.206.41.246/assest/background/defaultid3.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid3_thumb.jpg"
    }, {
        "backgroundId": "_defaultid4",
        "background": "123.206.41.246/assest/background/defaultid4.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid4_thumb.jpg"
    }, {
        "backgroundId": "_defaultid5",
        "background": "123.206.41.246/assest/background/defaultid5.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid5_thumb.jpg"
    }, {
        "backgroundId": "_defaultid6",
        "background": "123.206.41.246/assest/background/defaultid6.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid6_thumb.jpg"
    }, {
        "backgroundId": "_defaultid7",
        "background": "123.206.41.246/assest/background/defaultid7.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid7_thumb.jpg"
    }, {
        "backgroundId": "_defaultid8",
        "background": "123.206.41.246/assest/background/defaultid8.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid8_thumb.jpg"
    }, {
        "backgroundId": "_defaultid9",
        "background": "123.206.41.246/assest/background/defaultid9.jpg",
        "thumbBackground": "123.206.41.246/assest/background/defaultid9_thumb.jpg"
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
        "background": "123.206.41.246/assest/signup_background/defaultid1.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid1_thumb.jpg"
    }, {
        "backgroundId": "_defaultid2",
        "background": "123.206.41.246/assest/signup_background/defaultid2.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid2_thumb.jpg"
    }, {
        "backgroundId": "_defaultid3",
        "background": "123.206.41.246/assest/signup_background/defaultid3.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid3_thumb.jpg"
    }, {
        "backgroundId": "_defaultid4",
        "background": "123.206.41.246/assest/signup_background/defaultid4.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid4_thumb.jpg"
    }, {
        "backgroundId": "_defaultid5",
        "background": "123.206.41.246/assest/signup_background/defaultid5.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid5_thumb.jpg"
    }, {
        "backgroundId": "_defaultid6",
        "background": "123.206.41.246/assest/signup_background/defaultid6.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid6_thumb.jpg"
    }, {
        "backgroundId": "_defaultid7",
        "background": "123.206.41.246/assest/signup_background/defaultid7.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid7_thumb.jpg"
    }, {
        "backgroundId": "_defaultid8",
        "background": "123.206.41.246/assest/signup_background/defaultid8.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid8_thumb.jpg"
    }, {
        "backgroundId": "_defaultid9",
        "background": "123.206.41.246/assest/signup_background/defaultid9.jpg",
        "thumbBackground": "123.206.41.246/assest/signup_background/defaultid9_thumb.jpg"
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
    "icon": "123.206.41.246/assest/guide/guide_plane.png",
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
    "icon": "123.206.41.246/assest/guide/guide_meetingPlace.png",
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
    "icon": "123.206.41.246/assest/guide/guide_dinner.png",
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
    "icon": "123.206.41.246/assest/guide/guide_stay.png",
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
    "icon": "123.206.41.246/assest/guide/guide_customize.png",
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
    "icon": "123.206.41.246/assest/guide/guide_plane.png",
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
    "icon": "123.206.41.246/assest/guide/guide_meetingPlace.png",
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
    "icon": "123.206.41.246/assest/guide/guide_dinner.png",
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
    "icon": "123.206.41.246/assest/guide/guide_stay.png",
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
    "icon": "123.206.41.246/assest/guide/guide_customize.png",
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
    "icon": "123.206.41.246/assest/guide/guide_plane.png",
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
    "icon": "123.206.41.246/assest/guide/guide_meetingPlace.png",
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
    "icon": "123.206.41.246/assest/guide/guide_dinner.png",
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
    "icon": "123.206.41.246/assest/guide/guide_stay.png",
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
    "icon": "123.206.41.246/assest/guide/guide_customize.png",
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
    "backgroundImage": "123.206.41.246/assest/sign_up/default.jpg",
    "thumbBackground": "123.206.41.246/assest/sign_up/default_thumb.jpg",
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
        "icon": "http://123.206.41.246/files/app/app_guide.png",
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
        "icon": "http://123.206.41.246/files/app/app_info.png",
        "i18nName": {
            "zh_CN": "我的信息",
            "zh_TW": "我的信息",
            "en_US": "My Info"
        },
        "name": "我的信息",
        "enable": false,
        "url": "#/conferenceMy?conferenceId=${conferenceId}"
    }, {
        "appId": "_signin",
        "icon": "http://123.206.41.246/files/app/app_signin.png",
        "i18nName": {
            "zh_CN": "大会签到",
            "zh_TW": "大會簽到",
            "en_US": "Check In"
        },
        "name": "大会签到",
        "enable": false,
        "url": "#/conferenceSign"
    }, {
        "appId": "_rank",
        "icon": "http://123.206.41.246/files/app/app_agenda.png",
        "i18nName": {
            "zh_CN": "排行榜",
            "zh_TW": "排行榜",
            "en_US": "Agenda"
        },
        "name": "答题排行榜",
        "enable": false,
        "url": "#/question/rank"
    }, {
        "appId": "_question",
        "icon": "http://123.206.41.246/files/app/app_collect_face.png",
        "i18nName": {
            "zh_CN": "答题闯关",
            "zh_TW": "答题闯关",
            "en_US": "question"
        },
        "name": "答题闯关",
        "enable": false,
        "url": "#/question/list"
    },{
        "appId": "_barrage",
        "icon": "http://123.206.41.246/files/app//app_barrage.png",
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
