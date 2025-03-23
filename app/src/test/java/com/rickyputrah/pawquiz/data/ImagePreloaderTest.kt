package com.rickyputrah.pawquiz.data

import android.content.Context
import coil3.ImageLoader
import coil3.request.ImageRequest
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ImagePreloaderTest {

    private val mockImageLoader: ImageLoader = mockk(relaxed = true)
    private val mockContext: Context = mockk()
    private lateinit var imagePreloader: ImagePreloaderImpl

    @Before
    fun setup() {
        imagePreloader = ImagePreloaderImpl(
            imageLoader = mockImageLoader,
            context = mockContext,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `When preload image with url Then correct url is enqueue`() = runTest {
        val testUrl = "https://example.com/image.jpg"
        val requestSlot = slot<ImageRequest>()

        imagePreloader.preloadImage(testUrl)

        verify { mockImageLoader.enqueue(capture(requestSlot)) }

        assert(requestSlot.captured.data == testUrl)
    }
}