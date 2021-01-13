package com.example.spotifyclone.framework.datasource.cache.mappers

import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.business.domain.util.DomainMapper
import com.example.spotifyclone.framework.datasource.cache.model.TrackEntity

class TrackCacheMapper : DomainMapper<TrackEntity, TrackObject> {

    override fun mapToDomainModel(model: TrackEntity): TrackObject {
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

    override fun mapFromDomainModel(domainModel: TrackObject): TrackEntity {
        return TrackEntity(
            album = domainModel.album,
            artists = domainModel.artists,
            duration_ms = domainModel.duration_ms,
            id = domainModel.id,
            name = domainModel.name,
            type = domainModel.type,
            uri = domainModel.uri
        )
    }

    fun mapListToDomainModel(models: List<TrackEntity>): List<TrackObject> {
        return models.map { mapToDomainModel(it) }
    }

    fun mapListFromDomainModel(domainModels: List<TrackObject>): List<TrackEntity> {
        return domainModels.map { mapFromDomainModel(it) }
    }
}
