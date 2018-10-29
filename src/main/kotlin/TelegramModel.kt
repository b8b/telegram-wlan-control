import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
        @SerialName("id") val id: Int,
        @SerialName("first_name") val firstName: String? = null,
        @SerialName("last_name") val lastName: String? = null,
        @SerialName("username") val username: String? = null,
        @SerialName("is_bot") val bot: Boolean = false,
        @SerialName("language_code") val languageCode: String? = null
)

@Serializable
data class Chat(
        @SerialName("id") val id: Int,
        @SerialName("type") val type: String? = null,
        @SerialName("title") val title: String? = null,
        @SerialName("username") val username: String? = null,
        @SerialName("first_name") val firstName: String? = null,
        @SerialName("last_name") val lastName: String? = null,
        @SerialName("all_members_are_administrators") val allAdmins: Boolean? = null
)

@Serializable
data class Audio(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("duration") val duration: Int? = null,
        @SerialName("performer") val performer: String? = null,
        @SerialName("title") val title: String? = null,
        @SerialName("mime_type") val mimeType: String? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class PhotoSize(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("width") val width: Int? = null,
        @SerialName("height") val height: Int? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Document(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("thumb") val thumb: PhotoSize? = null,
        @SerialName("file_name") val fileName: String? = null,
        @SerialName("mime_type") val mimeType: String? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Animation(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("thumb") val thumb: PhotoSize? = null,
        @SerialName("file_name") val fileName: String? = null,
        @SerialName("mime_type") val mimeType: String? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Game(
        @SerialName("title") val title: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("photo") val photo: List<PhotoSize>? = null,
        @SerialName("text") val text: String? = null,
        @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
        @SerialName("animation") val animation: Animation? = null
)

@Serializable
data class Sticker(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("width") val width: Int? = null,
        @SerialName("height") val height: Int? = null,
        @SerialName("thumb") val thumb: PhotoSize? = null,
        @SerialName("emoji") val emoji: String? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Video(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("width") val width: Int? = null,
        @SerialName("height") val height: Int? = null,
        @SerialName("duration") val duration: Int? = null,
        @SerialName("thumb") val thumb: PhotoSize? = null,
        @SerialName("mime_type") val mimeType: String? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class VideoNote(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("length") val length: Int? = null,
        @SerialName("duration") val duration: Int? = null,
        @SerialName("thumb") val thumb: PhotoSize? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Voice(
        @SerialName("file_id") val fileId: String? = null,
        @SerialName("duration") val duration: Int? = null,
        @SerialName("mime_type") val mimeType: String? = null,
        @SerialName("file_size") val fileSize: Int? = null
)

@Serializable
data class Contact(
        @SerialName("phone_number") val phoneNumber: String? = null,
        @SerialName("first_name") val firstName: String? = null,
        @SerialName("last_name") val lastName: String? = null,
        @SerialName("user_id") val userId: Int? = null
)

@Serializable
data class Location(
        @SerialName("longitude") val longitude: Double? = null,
        @SerialName("latitude") val latitude: Double? = null
)

@Serializable
data class Venue(
        @SerialName("location") val location: Location? = null,
        @SerialName("title") val title: String? = null,
        @SerialName("address") val address: String? = null,
        @SerialName("foursquare_id") val foursquareId: String? = null
)

@Serializable
data class Invoice(
        @SerialName("title") val title: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("start_parameter") val startParameter: String? = null,
        @SerialName("currency") val currency: String? = null,
        @SerialName("total_amount") val totalAmount: Int? = null
)

@Serializable
data class ShippingAddress(
        @SerialName("country_code") val countryCode: String? = null,
        @SerialName("state") val state: String? = null,
        @SerialName("city") val city: String? = null,
        @SerialName("street_line1") val streetLine1: String? = null,
        @SerialName("street_line2") val streetLine2: String? = null,
        @SerialName("post_code") val postCode: String? = null
)

@Serializable
data class OrderInfo(
        @SerialName("name") val name: String? = null,
        @SerialName("phone_number") val phoneNumber: String? = null,
        @SerialName("email") val email: String? = null,
        @SerialName("shipping_address") val shippingAddress: ShippingAddress? = null
)

@Serializable
data class SuccessfulPayment(
        @SerialName("currency") val currency: String? = null,
        @SerialName("total_amount") val totalAmount: Int? = null,
        @SerialName("invoice_payload") val invoicePayload: String? = null,
        @SerialName("shipping_option_id") val shippingOptionId: String? = null,
        @SerialName("order_info") val orderInfo: OrderInfo? = null,
        @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String? = null,
        @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String? = null
)

@Serializable
data class MessageEntity(
        @SerialName("type") val type: String? = null,
        @SerialName("offset") val offset: Int? = null,
        @SerialName("length") val length: Int? = null,
        @SerialName("url") val url: String? = null,
        @SerialName("user") val user: User? = null
)

@Serializable
data class Message(
        @SerialName("message_id") val messageId: Int,
        @SerialName("from") val from: User? = null,
        @SerialName("date") val date: Int? = null,
        @SerialName("chat") val chat: Chat? = null,
        @SerialName("forward_from") val forwardFrom: User? = null,
        @SerialName("forward_from_chat") val forwardFromChat: Chat? = null,
        @SerialName("forward_from_message_id") val forwardFromMessageId: Int? = null,
        @SerialName("forward_date") val forwardDate: Int? = null,
        @SerialName("reply_to_message") val replyToMessage: Message? = null,
        @SerialName("edit_date") val editDate: Int? = null,
        @SerialName("text") val text: String? = null,
        @SerialName("entities") val entities: List<MessageEntity>? = null,
        @SerialName("audio") val audio: Audio? = null,
        @SerialName("document") val document: Document? = null,
        @SerialName("game") val game: Game? = null,
        @SerialName("photo") val photo: List<PhotoSize>? = null,
        @SerialName("sticker") val sticker: Sticker? = null,
        @SerialName("video") val video: Video? = null,
        @SerialName("video_note") val videoNote: VideoNote? = null,
        @SerialName("voice") val voice: Voice? = null,
        @SerialName("caption") val caption: String? = null,
        @SerialName("contact") val contact: Contact? = null,
        @SerialName("location") val location: Location? = null,
        @SerialName("venue") val venue: Venue? = null,
        @SerialName("new_chat_members") val newChatMembers: List<User>? = null,
        @SerialName("left_chat_member") val leftChatMember: User? = null,
        @SerialName("new_chat_title") val newChatTitle: String? = null,
        @SerialName("new_chat_photo") val newChatPhoto: List<PhotoSize>? = null,
        @SerialName("delete_chat_photo") val deleteChatPhoto: Boolean? = null,
        @SerialName("group_chat_created") val groupChatCreated: Boolean? = null,
        @SerialName("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,
        @SerialName("channel_chat_created") val channelChatCreated: Boolean? = null,
        @SerialName("migrate_to_chat_id") val migrateToChatId: Int? = null,
        @SerialName("migrate_from_chat_id") val migrateFromChatId: Int? = null,
        @SerialName("pinned_message") val pinnedMessage: Message? = null,
        @SerialName("invoice") val invoice: Invoice? = null,
        @SerialName("successful_payment") val successfulPayment: SuccessfulPayment? = null
)

class InlineQuery
class ChosenInlineResult

class CallbackQuery
class ShippingQuery
class PreCheckoutQuery

@Serializable
data class Update(
        @SerialName("update_id") val updateId: Int,
        @SerialName("message") val message: Message?,
        @SerialName("edited_message") val editedMessage: Message?,
        @SerialName("channel_post") val channelPost: Message?,
        @SerialName("edited_channel_post") val editedChannelPost: Message?,
        @SerialName("inline_query") val inlineQuery: InlineQuery?,
        @SerialName("chosen_inline_result") val chosenInlineResult: ChosenInlineResult?,
        @SerialName("callback_query") val callbackQuery: CallbackQuery?,
        @SerialName("shipping_query") val shippingQuery: ShippingQuery?,
        @SerialName("pre_checkout_query") val preCheckoutQuery: PreCheckoutQuery?
)

@Serializable
data class ChatMember(
        val user: User,
        val status: String,
        @SerialName("can_be_edited") val canBeEdited: Boolean,
        @SerialName("can_change_info") val canChangeInfo: Boolean,
        @SerialName("can_delete_messages") val canDeleteMessages: Boolean,
        @SerialName("can_invite_users") val canInviteUsers: Boolean,
        @SerialName("can_restrict_members") val canRestrictMembers: Boolean,
        @SerialName("can_promote_members") val canPromoteMembers: Boolean
)
