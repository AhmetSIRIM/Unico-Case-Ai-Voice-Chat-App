package com.ahmetsirim.data.exception

/**
 * Exception thrown when a generative AI model is not supported or implemented.
 *
 * @param modelName The name of the unsupported AI model.
 */
class UnsupportedGenerativeAiException(modelName: String) : Exception("$modelName not implemented yet")