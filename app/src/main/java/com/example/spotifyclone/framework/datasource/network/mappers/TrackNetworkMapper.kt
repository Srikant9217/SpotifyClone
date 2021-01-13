package com.example.spotifyclone.framework.datasource.network.mappers

import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.business.domain.util.DomainMapper
import com.example.spotifyclone.framework.datasource.network.model.TrackDto

class TrackNetworkMapper : DomainMapper<TrackDto, TrackObject> {

    override fun mapToDomainModel(model: TrackDto): TrackObject {
        return TrackObject(
            album = model.album,
            artists = model.artists,
            duration_ms = model.duration_ms,
            id = model.id,
            name = model.name,
            type = model.type,
            uri = model.uri
        )
    }

    override fun mapFromDomainModel(domainModel: TrackObject): TrackDto {
        return TrackDto(
            album = domainModel.album,
            artists = domainModel.artists,
            duration_ms = domainModel.duration_ms,
            id = domainModel.id,
            name = domainModel.name,
            type = domainModel.type,
            uri = domainModel.uri
        )
    }

    fun mapListToDomainModel(models: List<TrackDto>): List<TrackObject> {
        return models.map { mapToDomainModel(it) }
    }

    fun mapListFromDomainModel(domainModels: List<TrackObject>): List<TrackDto> {
        return domainModels.map { mapFromDomainModel(it) }
    }
}
