package HeyDoctor.HeyDoctor_Backend.domain.location.util;

import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationResponseDto;

public class RegionMatcher {

    /**
     * 재난문자/날씨 지역 문자열이 사용자의 행정구역과 매칭되는지 확인
     *
     * @param regionString   API에서 내려온 지역 문자열 (예: "서울특별시 종로구 청운효자동")
     * @param userLocation   사용자 행정구역 정보
     * @return true: 해당 지역 포함, false: 미포함
     */
    public static boolean matchRegion(String regionString, LocationResponseDto userLocation) {
        if (regionString == null || userLocation == null) {
            return false;
        }

        // 시/도 비교
        if (!regionString.contains(userLocation.getSido())) {
            return false;
        }

        // 시/군/구 비교
        if (!regionString.contains(userLocation.getSigungu())) {
            return false;
        }

        // 읍/면/동 정보가 존재하면 포함 여부 확인
        String eupmyeondong = userLocation.getEupmyeondong();
        if (eupmyeondong != null && !eupmyeondong.isEmpty()) {
            return regionString.contains(eupmyeondong);
        }

        return true;
    }
}
