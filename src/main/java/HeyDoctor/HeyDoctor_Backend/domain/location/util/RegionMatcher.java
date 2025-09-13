package HeyDoctor.HeyDoctor_Backend.domain.location.util;

import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationResponseDto;

public class RegionMatcher {

    /**
     * 재난문자 API의 지역 문자열이 사용자의 행정구역을 포함하는지 확인합니다.
     * 공백, 쉼표 등 불규칙한 형식을 처리하여 안정성을 높였습니다.
     *
     * @param alertRegionString API에서 받은 지역 문자열 (예: "서울특별시 강서구 , 서울특별시 양천구 ")
     * @param userLocation      사용자 행정구역 정보 (예: sido="서울특별시", sigungu="강서구")
     * @return true: 해당 지역 포함, false: 미포함
     */
    public static boolean matchRegion(String alertRegionString, LocationResponseDto userLocation) {
        if (alertRegionString == null || userLocation == null || userLocation.getSido() == null || userLocation.getSigungu() == null) {
            return false;
        }

        // 1. 사용자 위치를 "시도시군구" 형태의 공백 없는 문자열로 만듭니다. (예: "서울특별시강서구")
        String userFullLocation = userLocation.getSido() + userLocation.getSigungu();

        // 2. 재난문자 수신 지역(alertRegionString)에서 모든 공백을 제거합니다.
        // 예: " 서울특별시  강서구 , 서울특별시  양천구 " -> "서울특별시강서구,서울특별시양천구"
        String cleanedAlertRegion = alertRegionString.replaceAll("\\s+", "");

        // 3. 공백이 제거된 재난문자 수신 지역에, 공백 없는 사용자 위치가 포함되어 있는지 확인합니다.
        // 예: "서울특별시강서구,서울특별시양천구".contains("서울특별시강서구") -> true
        return cleanedAlertRegion.contains(userFullLocation);
    }
}