import kotlinx.serialization.SerialName
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class User(
        @SerialName("id") val id: Int,
        @Optional @SerialName("first_name") val firstName: String? = null,
        @Optional @SerialName("last_name") val lastName: String? = null,
        @Optional @SerialName("username") val username: String? = null,
        @Optional @SerialName("is_bot") val bot: Boolean = false,
        @Optional @SerialName("language_code") val languageCode: String? = null
)

@Serializable
data class Chat(
        @SerialName("id") val id: Int,
        @Optional @SerialName("type") val type: String? = null,
        @Optional @SerialName("title") val title: String? = null,
        @Optional @SerialName("username") val username: String? = null,
        @Optional @SerialName("first_name") val firstName: String? = null,
        @Optional @SerialName("last_name") val lastName: String? = null,
        @Optional @SerialName("all_members_are_administrators") val allAdmins: Boolean? = null
)

@Serializable
data class Audio(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("duration") val duration: Int? = null,
        @Optional @SerialName("performer") val performer: String? = null,
        @Optional @SerialName("title") val title: String? = null,
        @Optional @SerialName("mime_type") val mimeType: String? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class PhotoSize(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("width") val width: Int? = null,
        @Optional @SerialName("height") val height: Int? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Document(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("thumb") val thumb: PhotoSize? = null,
        @Optional @SerialName("file_name") val fileName: String? = null,
        @Optional @SerialName("mime_type") val mimeType: String? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Animation(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("thumb") val thumb: PhotoSize? = null,
        @Optional @SerialName("file_name") val fileName: String? = null,
        @Optional @SerialName("mime_type") val mimeType: String? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Game(
        @Optional @SerialName("title") val title: String? = null,
        @Optional @SerialName("description") val description: String? = null,
        @Optional @SerialName("photo") val photo: List<PhotoSize>? = null,
        @Optional @SerialName("text") val text: String? = null,
        @Optional @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
        @Optional @SerialName("animation") val animation: Animation? = null
)

@Serializable
data class Sticker(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("width") val width: Int? = null,
        @Optional @SerialName("height") val height: Int? = null,
        @Optional @SerialName("thumb") val thumb: PhotoSize? = null,
        @Optional @SerialName("emoji") val emoji: String? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Video(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("width") val width: Int? = null,
        @Optional @SerialName("height") val height: Int? = null,
        @Optional @SerialName("duration") val duration: Int? = null,
        @Optional @SerialName("thumb") val thumb: PhotoSize? = null,
        @Optional @SerialName("mime_type") val mimeType: String? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class VideoNote(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("length") val length: Int? = null,
        @Optional @SerialName("duration") val duration: Int? = null,
        @Optional @SerialName("thumb") val thumb: PhotoSize? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Voice(
        @Optional @SerialName("file_id") val fileId: String? = null,
        @Optional @SerialName("duration") val duration: Int? = null,
        @Optional @SerialName("mime_type") val mimeType: String? = null,
        @Optional @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Contact(
        @Optional @SerialName("phone_number") val phoneNumber: String? = null,
        @Optional @SerialName("first_name") val firstName: String? = null,
        @Optional @SerialName("last_name") val lastName: String? = null,
        @Optional @SerialName("user_id") val userId: Int? = null
)

@Serializable
data class Location(
        @Optional @SerialName("longitude") val longitude: Double? = null,
        @Optional @SerialName("latitude") val latitude: Double? = null
)

@Serializable
data class Venue(
        @Optional @SerialName("location") val location: Location? = null,
        @Optional @SerialName("title") val title: String? = null,
        @Optional @SerialName("address") val address: String? = null,
        @Optional @SerialName("foursquare_id") val foursquareId: String? = null
)

@Serializable
data class Invoice(
        @Optional @SerialName("title") val title: String? = null,
        @Optional @SerialName("description") val description: String? = null,
        @Optional @SerialName("start_parameter") val startParameter: String? = null,
        @Optional @SerialName("currency") val currency: String? = null,
        @Optional @SerialName("total_amount") val totalAmount: Int? = null
)

@Serializable
data class ShippingAddress(
        @Optional @SerialName("country_code") val countryCode: String? = null,
        @Optional @SerialName("state") val state: String? = null,
        @Optional @SerialName("city") val city: String? = null,
        @Optional @SerialName("street_line1") val streetLine1: String? = null,
        @Optional @SerialName("street_line2") val streetLine2: String? = null,
        @Optional @SerialName("post_code") val postCode: String? = null
)

@Serializable
data class OrderInfo(
        @Optional @SerialName("name") val name: String? = null,
        @Optional @SerialName("phone_number") val phoneNumber: String? = null,
        @Optional @SerialName("email") val email: String? = null,
        @Optional @SerialName("shipping_address") val shippingAddress: ShippingAddress? = null
)

@Serializable
data class SuccessfulPayment(
        @Optional @SerialName("currency") val currency: String? = null,
        @Optional @SerialName("total_amount") val totalAmount: Int? = null,
        @Optional @SerialName("invoice_payload") val invoicePayload: String? = null,
        @Optional @SerialName("shipping_option_id") val shippingOptionId: String? = null,
        @Optional @SerialName("order_info") val orderInfo: OrderInfo? = null,
        @Optional @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String? = null,
        @Optional @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String? = null
)

@Serializable
data class MessageEntity(
        @Optional @SerialName("type") val type: String? = null,
        @Optional @SerialName("offset") val offset: Int? = null,
        @Optional @SerialName("length") val length: Int? = null,
        @Optional @SerialName("url") val url: String? = null,
        @Optional @SerialName("user") val user: User? = null
)

@Serializable
data class Message(
        @SerialName("message_id") val messageId: Int,
        @Optional @SerialName("from") val from: User? = null,
        @Optional @SerialName("date") val date: Int? = null,
        @Optional @SerialName("chat") val chat: Chat? = null,
        @Optional @SerialName("forward_from") val forwardFrom: User? = null,
        @Optional @SerialName("forward_from_chat") val forwardFromChat: Chat? = null,
        @Optional @SerialName("forward_from_message_id") val forwardFromMessageId: Int? = null,
        @Optional @SerialName("forward_date") val forwardDate: Int? = null,
        @Optional @SerialName("reply_to_message") val replyToMessage: Message? = null,
        @Optional @SerialName("edit_date") val editDate: Int? = null,
        @Optional @SerialName("text") val text: String? = null,
        @Optional @SerialName("entities") val entities: List<MessageEntity>? = null,
        @Optional @SerialName("audio") val audio: Audio? = null,
        @Optional @SerialName("document") val document: Document? = null,
        @Optional @SerialName("game") val game: Game? = null,
        @Optional @SerialName("photo") val photo: List<PhotoSize>? = null,
        @Optional @SerialName("sticker") val sticker: Sticker? = null,
        @Optional @SerialName("video") val video: Video? = null,
        @Optional @SerialName("video_note") val videoNote: VideoNote? = null,
        @Optional @SerialName("voice") val voice: Voice? = null,
        @Optional @SerialName("caption") val caption: String? = null,
        @Optional @SerialName("contact") val contact: Contact? = null,
        @Optional @SerialName("location") val location: Location? = null,
        @Optional @SerialName("venue") val venue: Venue? = null,
        @Optional @SerialName("new_chat_members") val newChatMembers: List<User>? = null,
        @Optional @SerialName("left_chat_member") val leftChatMember: User? = null,
        @Optional @SerialName("new_chat_title") val newChatTitle: String? = null,
        @Optional @SerialName("new_chat_photo") val newChatPhoto: List<PhotoSize>? = null,
        @Optional @SerialName("delete_chat_photo") val deleteChatPhoto: Boolean? = null,
        @Optional @SerialName("group_chat_created") val groupChatCreated: Boolean? = null,
        @Optional @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,
        @Optional @SerialName("channel_chat_created") val channelChatCreated: Boolean? = null,
        @Optional @SerialName("migrate_to_chat_id") val migrateToChatId: Int? = null,
        @Optional @SerialName("migrate_from_chat_id") val migrateFromChatId: Int? = null,
        @Optional @SerialName("pinned_message") val pinnedMessage: Message? = null,
        @Optional @SerialName("invoice") val invoice: Invoice? = null,
        @Optional @SerialName("successful_payment") val successfulPayment: SuccessfulPayment? = null
)

class InlineQuery
class ChosenInlineResult

class CallbackQuery
class ShippingQuery
class PreCheckoutQuery

@Serializable
data class Update(
        @SerialName("update_id") val updateId: Int,
        @Optional @SerialName("message") val message: Message? = null,
        @Optional @SerialName("edited_message") val editedMessage: Message? = null,
        @Optional @SerialName("channel_post") val channelPost: Message? = null,
        @Optional @SerialName("edited_channel_post") val editedChannelPost: Message? = null,
        @Optional @SerialName("inline_query") val inlineQuery: InlineQuery? = null,
        @Optional @SerialName("chosen_inline_result") val chosenInlineResult: ChosenInlineResult? = null,
        @Optional @SerialName("callback_query") val callbackQuery: CallbackQuery? = null,
        @Optional @SerialName("shipping_query") val shippingQuery: ShippingQuery? = null,
        @Optional @SerialName("pre_checkout_query") val preCheckoutQuery: PreCheckoutQuery? = null
)

@Serializable
data class ChatMember(
        val user: User,
        val status: String,
        @Optional @SerialName("can_be_edited") val canBeEdited: Boolean = false,
        @Optional @SerialName("can_change_info") val canChangeInfo: Boolean = false,
        @Optional @SerialName("can_delete_messages") val canDeleteMessages: Boolean = false,
        @Optional @SerialName("can_invite_users") val canInviteUsers: Boolean = false,
        @Optional @SerialName("can_restrict_members") val canRestrictMembers: Boolean = false,
        @Optional @SerialName("can_promote_members") val canPromoteMembers: Boolean = false
)
