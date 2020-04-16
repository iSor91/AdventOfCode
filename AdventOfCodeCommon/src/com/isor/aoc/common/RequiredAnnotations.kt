package com.isor.aoc.common

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class RequiredAnnotations (val annotations: Array<KClass<out Annotation>>){
}